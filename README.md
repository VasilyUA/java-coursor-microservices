# Microservice

#### For will add new service need to add new service in `docker-compose.yml` and also to `.gitlab/workflows/main.yml`

#### For run services need to run this command:
```bash
# Run all services
docker-compose up -d

# Run a one service
docker-compose up -d <service-name>

# To start several instances of the service, you can run the following command:
docker-compose up --scale <service-name>=<number-of-instances> -d
```

#### Services:
1. [Security service](./security)
2. [NoSQL service](./NoSqlDB)

#### Documentation `README.md` for services:
1. [Security service](./security/README.md)
2. [NoSQL service](./NoSqlDB/README.md)

