# ─────────────────────────────────────────
# ETAPA 1: Compilar el proyecto con Maven
# ─────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

# ─────────────────────────────────────────
# ETAPA 2: Imagen final de ejecución
# ─────────────────────────────────────────
FROM eclipse-temurin:21-jre

RUN apt-get update && apt-get install -y \
    libgl1 \
    libgtk-3-0 \
    libx11-6 \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    openjfx \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=build /app/target/MedicArte-1.0-SNAPSHOT.jar app.jar

ENV DB_HOST=db
ENV DB_PORT=5432
ENV DB_NAME=MedicArte
ENV DB_USER=postgres
ENV DB_PASS=Erkenenpostgres23
ENV DISPLAY=:0

ENTRYPOINT ["java", \
    "--module-path", "/usr/share/openjfx/lib", \
    "--add-modules", "javafx.controls,javafx.fxml,javafx.graphics", \
    "-jar", "app.jar"]
