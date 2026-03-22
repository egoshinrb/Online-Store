package com.onlinestore.server.service;

import com.onlinestore.server.model.dto.OrderDtos;
import com.onlinestore.server.model.entity.OrderItem;
import com.onlinestore.server.model.entity.PaymentMethod;
import com.onlinestore.server.model.entity.PaymentStatus;
import com.onlinestore.server.model.entity.Product;
import com.onlinestore.server.model.entity.StoreOrder;
import com.onlinestore.server.model.entity.User;
import com.onlinestore.server.model.entity.UserAddress;
import com.onlinestore.server.repository.OrderItemRepository;
import com.onlinestore.server.repository.ProductRepository;
import com.onlinestore.server.repository.StoreOrderRepository;
import com.onlinestore.server.repository.UserAddressRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class OrderService {

    private final StoreOrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserAddressRepository userAddressRepository;
    private final RealtimeNotificationService realtimeNotificationService;

    public OrderService(
            StoreOrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository,
            UserAddressRepository userAddressRepository,
            RealtimeNotificationService realtimeNotificationService
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userAddressRepository = userAddressRepository;
        this.realtimeNotificationService = realtimeNotificationService;
    }

    @Transactional
    public OrderDtos.OrderDto create(User user, OrderDtos.CreateOrderRequest req) {
        if (req.items() == null || req.items().isEmpty()) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Empty order");
        }
        UserAddress address = null;
        String snapshot = req.addressSnapshot();
        if (req.deliveryAddressId() != null) {
            address = userAddressRepository.findById(req.deliveryAddressId())
                    .filter(a -> a.getUser().getId().equals(user.getId()))
                    .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Address not found"));
            if (snapshot == null || snapshot.isBlank()) {
                snapshot = address.getAddressLine();
            }
        }
        if (snapshot == null || snapshot.isBlank()) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Address is required");
        }

        BigDecimal total = BigDecimal.ZERO;
        List<OrderLine> lines = new ArrayList<>();
        for (OrderDtos.OrderItemRequest line : req.items()) {
            Product p = productRepository.findById(line.productId())
                    .orElseThrow(() -> new HttpStatusException(HttpStatus.BAD_REQUEST, "Unknown product: " + line.productId()));
            if (p.getStock() < line.quantity()) {
                throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for " + p.getName());
            }
            BigDecimal lineTotal = p.getPrice().multiply(BigDecimal.valueOf(line.quantity()));
            total = total.add(lineTotal);
            lines.add(new OrderLine(p, line.quantity()));
        }

        StoreOrder order = new StoreOrder();
        order.setUser(user);
        order.setTotal(total);
        order.setAddressSnapshot(snapshot);
        order.setDeliveryAddress(address);
        order.setPaymentMethod(req.paymentMethod());
        if (req.paymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {
            order.setPaymentStatus(PaymentStatus.PENDING);
        } else {
            order.setPaymentStatus(PaymentStatus.PENDING);
        }
        orderRepository.save(order);

        for (OrderLine line : lines) {
            OrderItem oi = new OrderItem();
            oi.setOrderId(order.getId());
            oi.setProductId(line.product().getId());
            oi.setQuantity(line.qty());
            oi.setPrice(line.product().getPrice());
            orderItemRepository.save(oi);
            Product p = line.product();
            p.setStock(p.getStock() - line.qty());
            productRepository.save(p);
        }

        realtimeNotificationService.notifyOrderStatus(user.getId(), order.getId(), order.getStatus().name());

        return toDto(orderRepository.findById(order.getId()).orElse(order));
    }

    @Transactional(readOnly = true)
    public List<OrderDtos.OrderDto> list(User user) {
        return orderRepository.findByUser_IdOrderByCreatedAtDesc(user.getId()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDtos.OrderDto get(User user, Long id) {
        StoreOrder o = orderRepository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return toDto(o);
    }

    private OrderDtos.OrderDto toDto(StoreOrder o) {
        List<OrderItem> items = orderItemRepository.findByOrderId(o.getId());
        List<OrderDtos.OrderItemDto> itemDtos = items.stream()
                .map(i -> new OrderDtos.OrderItemDto(
                        i.getProductId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPrice()
                ))
                .collect(Collectors.toList());
        return new OrderDtos.OrderDto(
                o.getId(),
                o.getStatus(),
                o.getTotal(),
                o.getAddressSnapshot(),
                o.getDeliveryAddress() != null ? o.getDeliveryAddress().getId() : null,
                o.getPaymentMethod(),
                o.getPaymentStatus(),
                o.getCreatedAt(),
                itemDtos
        );
    }

    private record OrderLine(Product product, int qty) {
    }
}
