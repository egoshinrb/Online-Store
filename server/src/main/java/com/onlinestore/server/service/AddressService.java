package com.onlinestore.server.service;

import com.onlinestore.server.model.dto.AddressDtos;
import com.onlinestore.server.model.entity.User;
import com.onlinestore.server.model.entity.UserAddress;
import com.onlinestore.server.repository.UserAddressRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class AddressService {

    private final UserAddressRepository userAddressRepository;

    public AddressService(UserAddressRepository userAddressRepository) {
        this.userAddressRepository = userAddressRepository;
    }

    @Transactional
    public List<AddressDtos.AddressDto> list(User user) {
        return userAddressRepository.findByUser_IdOrderByIdAsc(user.getId()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressDtos.AddressDto create(User user, AddressDtos.AddressRequest req) {
        if (req.defaultAddress()) {
            userAddressRepository.findByUser_IdOrderByIdAsc(user.getId()).forEach(a -> {
                a.setDefaultAddress(false);
                userAddressRepository.save(a);
            });
        }
        UserAddress a = new UserAddress();
        a.setUser(user);
        a.setLabel(req.label());
        a.setAddressLine(req.addressLine());
        a.setLatitude(req.latitude());
        a.setLongitude(req.longitude());
        a.setDefaultAddress(req.defaultAddress());
        userAddressRepository.save(a);
        return toDto(a);
    }

    @Transactional
    public void delete(User user, Long id) {
        UserAddress a = userAddressRepository.findById(id)
                .filter(x -> x.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Address not found"));
        userAddressRepository.delete(a);
    }

    private AddressDtos.AddressDto toDto(UserAddress a) {
        return new AddressDtos.AddressDto(
                a.getId(),
                a.getLabel(),
                a.getAddressLine(),
                a.getLatitude(),
                a.getLongitude(),
                a.isDefaultAddress()
        );
    }
}
