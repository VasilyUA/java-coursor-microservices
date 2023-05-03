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

### Cursor current task HW #17 - Integration Testing  :
```
1)  Cover one of your previous homework with test. Achieve at least 80% test coverage.

2) Optional.  

2.1 Write CatService that makes REST call to https://catfact.ninja/fact

2.2 Write @SpringBootTest integration test on this service

2.3 Use Wiremock to mock catfact webserver

2.4 Use @TestProperty to change catfact’s URL from https://catfact.ninja to localhost:XXXX where XXXX - your Wiremock port
```

#### For check test coverage need to open directory `build/customJacocoReportDir/index.html` after run command:
```bash
./gradlew jacocoTestReport
```