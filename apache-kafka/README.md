# Apache-Kafka

### Cursor current task HW #16 - NoSql DB:
```
Зробити 2 аплікації Producer які будуть продюсити меседжі кожен в 2 топіки і створити 3 консюмери які будуть читати ці топіки і записувати дані в базу.

Меседжі можуть бути типу News i User
```

### Actual result:
```
Зробити 1 аплікацію 2 Producer які будуть продюсити меседжі кожен в 2 топіки і створити 3 консюмери які будуть читати ці топіки і записувати дані в базу.

Меседжі типу News i User
```

### For run project:
Need run docker services mongo and kafka: 
```bash
# run mongo services
docker-compose up -d MongoExpress

# run kafka services
docker-compose up -d KafkaUI
```