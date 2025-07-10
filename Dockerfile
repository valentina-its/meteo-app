
# Usa una base super leggera
FROM node:20-alpine3.19

# Imposta la cartella di lavoro
WORKDIR /app

# Copia solo i file package per installare le dipendenze
COPY package*.json ./

# Installa solo le dipendenze di produzione
RUN npm ci --only=production

# Copia il resto del codice
COPY target/meteo-0.0.1-SNAPSHOT.jar app.jar

# Espone la porta (modifica se serve)
EXPOSE 8080

# Avvia l'app
CMD ["npm", "start"]

# Comando di avvio
ENTRYPOINT ["java", "-jar", "app.jar"]


# ====== STAGE 1: Build frontend (Node.js/React) ======
FROM node:20-alpine3.19 AS frontend-build

WORKDIR /app

# Copia solo i file package e installa dipendenze
COPY package*.json ./
RUN npm ci

# Copia il resto del codice e costruisci il frontend
COPY target/meteo-0.0.1-SNAPSHOT.jar app.jar
RUN npm run build


# ====== STAGE 2: Build backend (Java) ======
FROM maven:3.9.6-eclipse-temurin-21-alpine AS backend-build

WORKDIR /app

# Copia solo i file necessari per la build Java
COPY pom.xml ./
COPY src ./src

# Costruisci il jar
RUN mvn clean package -DskipTests

# ====== STAGE 3: Immagine finale super leggera ======
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia il jar dal backend-build
COPY --from=backend-build /app/target/meteo-0.0.1-SNAPSHOT.jar app.jar

# (Opzionale) Copia la build frontend se serve servirla con il backend
# COPY --from=frontend-build /app/build ./public

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]