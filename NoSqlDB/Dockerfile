# Завантажуємо образ OpenJDK 17 з slim-образом Debian як базовий
FROM openjdk:17-slim

# Встановлюємо необхідні залежності
RUN apt-get update && \
    apt-get install -y unzip zip curl && \
    curl -s "https://get.sdkman.io" | bash && \
    bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install gradle"

# Додаємо SDKMAN до змінної PATH
ENV PATH="/root/.sdkman/candidates/gradle/current/bin:${PATH}"

# Копіюйте файли вашого проекту в контейнер
COPY build.gradle /usr/src/app/
COPY settings.gradle /usr/src/app/
COPY src /usr/src/app/src

# Зберємо проект за допомогою Gradle
WORKDIR /usr/src/app
RUN gradle build -x test

# Сетим порт для з'єднання з мікросервісом
EXPOSE 5001

# Запустіть jar-файл вашого проекту
CMD ["java", "-jar", "build/libs/no-sql-service-0.1.0.jar", "--spring.profiles.active=docker"]

