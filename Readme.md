# Online Store — продуктовый магазин

Монорепозиторий: **Micronaut (Java)** backend + **Android (Kotlin, Compose)** клиент.

## Сервер (`server/`)

### Требования
- JDK 17+
- PostgreSQL 16+ (или Docker)

### Запуск БД
```bash
docker compose up -d
```

### Переменные окружения (опционально)
| Переменная | Описание |
|------------|----------|
| `JWT_SECRET` | Секрет HMAC для JWT (≥ 256 бит в продакшене) |
| `JDBC_URL` | `jdbc:postgresql://localhost:5432/onlinestore` |
| `DB_USER` / `DB_PASSWORD` | Учётные данные PostgreSQL |

### Сборка и запуск
```bash
cd server
./gradlew run
```
Если нет Gradle: установите [Gradle](https://gradle.org/install/) или откройте проект в IDE и выполните `run`.

- **REST API**: `http://localhost:8080/api/...`
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html` (или `/swagger/views/swagger-ui` в зависимости от версии OpenAPI)
- **WebSocket уведомления**: `ws://localhost:8080/ws/notifications?access_token=<JWT>`

### Эмулятор Android
Backend доступен с эмулятора по `http://10.0.2.2:8080` (уже задано в `BuildConfig`).

---

## Android (`android-app/`)

- **minSdk 32**, **targetSdk 36**, **compileSdk 36**
- Jetpack Compose, Hilt, Retrofit, DataStore, Google Maps Compose (выбор точки на карте)

### Карты
В `gradle.properties` или при сборке задайте ключ:
```properties
MAPS_API_KEY=ваш_ключ_Google_Maps
```

### FCM (push)
Сервер сохраняет FCM-токены (`POST /api/subscriptions`). Для реальной отправки push настройте Firebase Admin SDK и переменную `GOOGLE_APPLICATION_CREDENTIALS` на сервере (см. `FcmNotificationService`).

Откройте папку `android-app` в Android Studio и соберите проект.

---

## Безопасность (кратко)
- Пароли: BCrypt (cost 12)
- JWT access + refresh (хеш refresh в БД)
- HTTPS/WSS в продакшене обязательны
- Секреты только из окружения

---

## Структура
```
Online-Store/
├── server/           # Micronaut + JPA + Flyway + WebSocket
├── android-app/      # Kotlin + Compose
├── docker-compose.yml
└── docs/PLAN.md
```
