# NoSqlDbApplication

### Cursor current task HW #16 - NoSql DB:
```
Створити User Document (id, firstName, LastName, email, age, isMarried)
Додати в базу 5 азерів за допомогою SpringData i 5 за допомогою mongosh (код додавання додати в ревю дз)
Реалізувати наступні операції використовуючи MongoDB через Spring Data

- Вивести юзера за: firstName, lastName, email
- Вивести всіх хто старше 18
- Вивести всіх одружених

Також всі ці операції виконати через mongosh i додати в дз файл.
```

#### Run mongosh example:
```bash
# docker exec -it <container name> mongosh -u <root-user> -p <password> --authenticationDatabase admin <YUUR DB NAME>:
docker exec -it ce48ce7c1199 mongosh -u root -p 55555 --authenticationDatabase admin NoSqlDB
```

#### Run mongosh example for check:
```bash
# don't forget to run docker-compose up -d before:
bash ./NoSqlDB/mongo_commands.sh
```