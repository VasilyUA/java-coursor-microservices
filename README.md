# Microservice

#### For will add new service need to add new service in `docker-compose.yml` and also to `.gitlab/workflows/main.yml`

#### For run services need to run this command:
```bash
# Run all services
docker-compose up -d

# Run a service
docker-compose run -d <service-name>
```

#### Services:
1. [Security service](./security)