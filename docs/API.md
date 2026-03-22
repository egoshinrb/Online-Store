# API (кратко)

Базовый URL: `http://localhost:8080` (с эмулятора: `http://10.0.2.2:8080`).

Заголовок для защищённых методов: `Authorization: Bearer <access_token>`.

| Метод | Путь | Описание |
|--------|------|----------|
| POST | `/api/auth/register` | Регистрация |
| POST | `/api/auth/login` | Вход |
| POST | `/api/auth/refresh` | Обновление токенов (тело: `refreshToken`) |
| POST | `/api/auth/logout` | Выход (тело: `refreshToken`) |
| GET | `/api/me` | Профиль |
| GET | `/api/categories` | Категории |
| GET | `/api/products` | Товары (`categoryId`, `q`, `minPrice`, `maxPrice`, `brand`, `inStockOnly`) |
| GET | `/api/products/{id}` | Товар |
| GET | `/api/favorites` | Избранное |
| POST | `/api/favorites/{productId}` | Добавить в избранное |
| DELETE | `/api/favorites/{productId}` | Удалить |
| POST | `/api/orders` | Создать заказ |
| GET | `/api/orders` | Заказы |
| GET | `/api/orders/{id}` | Заказ |
| GET | `/api/addresses` | Адреса |
| POST | `/api/addresses` | Новый адрес |
| DELETE | `/api/addresses/{id}` | Удалить адрес |
| POST | `/api/payments/confirm` | Подтверждение оплаты (заглушка) |
| POST | `/api/subscriptions` | FCM-токен |

**WebSocket:** `GET ws://host/ws/notifications?access_token=<JWT>` (или заголовок `Authorization: Bearer`).

**Swagger:** `/swagger-ui/index.html` после запуска сервера.
