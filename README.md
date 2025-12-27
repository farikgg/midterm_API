# Midterm API - Spring Boot Blog System

REST API сервис для управления блогом (Пользователи, Посты, Категории) с системой прав доступа и миграциями базы данных.

## Технологии

*   **Java 17**
*   **Spring Boot 3** (Web, Data JPA, Security, Validation)
*   **PostgreSQL** - основная база данных
*   **H2 Database** - база данных для тестов (изолированная среда)
*   **Flyway** - управление миграциями базы данных
*   **Docker & Docker Compose** - контейнеризация
*   **MapStruct** - маппинг DTO <-> Entity
*   **Lombok** - сокращение шаблонного кода

---

## Как запустить

### Через Docker (Рекомендуемый способ)

Убедитесь, что у вас установлен Docker и Docker Compose.

1.  Соберите и запустите контейнеры:
    ```bash
    docker-compose up --build
    ```
2.  Приложение будет доступно по адресу: `http://localhost:8888`

*При первом запуске Flyway автоматически создаст таблицы и заполнит базу тестовыми данными (Admin и User).*

---

## Аутентификация и Безопасность

Проект использует **Basic Authentication**.
Для выполнения любых запросов (кроме регистрации) необходимо передавать заголовок `Authorization`.

**Тестовые пользователи (создаются автоматически):**

| Role | Username | Password | Описание |
| :--- |:---------| :--- | :--- |
| **ADMIN** | `admin`  | `password` | Может управлять любым контентом |
| **USER** | `rafi`   | `password` | Управляет только своими постами |
| **USER** | `nurken` | `password` | Управляет только своими постами |

---

## API Endpoints

### Пользователи (Users)

| Метод | URL | Описание | Доступ |
| :--- | :--- | :--- | :--- |
| `POST` | `/users/register` | Регистрация нового пользователя | **Public** |
| `GET` | `/users` | Получить всех пользователей | Auth |
| `GET` | `/users/{id}` | Получить пользователя по ID | Auth |
| `PUT` | `/users/{id}` | Обновить данные пользователя | Owner / Admin |
| `DELETE` | `/users/{id}` | Удалить пользователя | Owner / Admin |

#### JSON для POST /users/register
```json
{
  "name": "new_user",
  "password": "my_secret_password"
}
```

---

### Посты (Posts)

*При создании поста автор подставляется автоматически (текущий залогиненный пользователь).*

| Метод | URL | Описание | Доступ |
| :--- | :--- | :--- | :--- |
| `GET` | `/posts` | Получить все посты | Auth |
| `GET` | `/posts/{id}` | Получить пост по ID | Auth |
| `POST` | `/posts` | Создать новый пост | Auth |
| `PUT` | `/posts/{id}` | Обновить пост | Owner / Admin |
| `DELETE` | `/posts/{id}` | Удалить пост | Owner / Admin |

#### JSON для POST /posts
```json
{
  "title": "Заголовок поста",
  "text": "Текст содержимого поста..."
}
```

---

### Категории (Categories)

| Метод | URL | Описание | Доступ |
| :--- | :--- | :--- | :--- |
| `GET` | `/categories` | Получить все категории | Auth |
| `POST` | `/categories` | Создать категорию | Auth |
| `PUT` | `/categories/{id}` | Обновить категорию | Auth |
| `DELETE` | `/categories/{id}` | Удалить категорию | Auth |

#### JSON для POST /categories
```json
{
  "name": "Java Development",
  "color": "#FF5733"
}
```

---

### Связи (Many-to-Many)

Управление категориями внутри постов.

| Метод | URL | Описание | Доступ |
| :--- | :--- | :--- | :--- |
| `POST` | `/categories/add-to-post/{postId}/{categoryId}` | Добавить категорию к посту | Owner / Admin |
| `DELETE` | `/categories/remove-from-post/{postId}/{categoryId}` | Удалить категорию у поста | Owner / Admin |

*(Тела запроса (Body) для этих методов нет, параметры передаются в URL)*