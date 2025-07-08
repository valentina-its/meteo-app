# Base image con JDK 17 (puoi usare anche 21 se lo usi)
FROM eclipse-temurin:17-jdk-alpine

# Cartella di lavoro nel container
WORKDIR /app

# Copia il JAR generato
COPY target/meteo-0.0.1-SNAPSHOT.jar app.jar

# Espone la porta dell'app
EXPOSE 8080

# Comando di avvio
ENTRYPOINT ["java", "-jar", "app.jar"]
