# SecurityApplication

### Cursor current task HW #(13,14,15) - Spring Security + Validation + Swagger:
```
Зробити Rest API для book entity + validation

Додати swagger API

Додати автентифікацію і створення нового юзера

Автентифікація через JWT

Додати можливість створення ногово юзера(реєстрація)

В  swagger- ui додати можливість передачі jwt токена, щоб мати доступ в свагері до секюрних ендроінтів
```

#### For run mysql bash need run command:
```bash
docker exec -it mysqldb mysql -u root -p
```


___
### Cursor current task HW #20 - Docker:
```
Створити два докер контейнера. Перший дістає дані з другого і повертає їх.

● Наприклад restful service і db.

● Один endpoint /info, який повертає текст або будь-які дані.

● Ці дані повинні зберігатись в базі даних.

● База знаходиться в контейнері.

● В базі знаходяться попередньо введені дані.

● http://localhost:1248/info

● Hints: use docker compose to connect two services
```

### Actual result:
● Create two containers: `security-app` and `nosql-app`

● Decided use Nginx as load balancer in [docker-compose](../docker-compose.yml) for run two services [security-app](http://localhost:3000/security) and [nosql-app](http://localhost:3000/nosql)

● Security-app can use endpoints `/api/user-nosql-service/info`, `/api/user-nosql-service/users` from nosql-app

● Endpoint `/api/user-nosql-service/users` can get list of users from nosql-app and save it to mongodb container database
