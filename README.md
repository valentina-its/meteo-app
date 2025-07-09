# 🌦️ Meteo App — Spring Boot + Docker

Una web app meteo semplice, elegante e containerizzata, costruita con **Spring Boot** e **WebClient** per interrogare l'API [Open-Meteo](https://open-meteo.com/) e generare dinamicamente un **grafico PNG** con le temperature. L'interfaccia utente è responsive, moderna e completamente stateless.

---

## 📸 Anteprima

![Anteprima interfaccia](docs/preview.png)

---

## 🧠 Descrizione del progetto

- Il backend Spring Boot interroga Open-Meteo tramite `WebClient` passando coordinate geografiche
- Riceve dati orari e giornalieri in JSON e genera un grafico temperatura usando una libreria Java (es. JFreeChart)
- Il grafico viene esposto tramite endpoint REST come immagine PNG
- Il frontend HTML/CSS consente la selezione della città con caricamento dinamico del grafico

---

## 🧰 Stack Tecnologico

| Componente         | Tecnologia                      |
|--------------------|----------------------------------|
| Linguaggio         | Java 17+                         |
| Framework backend  | Spring Boot 3.x                  |
| HTTP client        | Spring `WebClient`               |
| Generazione grafici| JFreeChart / Java2D              |
| Frontend statico   | HTML5, CSS3                      |
| Contenitore        | Docker                           |
| API esterna        | [Open-Meteo](https://open-meteo.com/) |

---

## 📂 Requisiti

- Java 17+
- Maven 3.x
- Docker Engine

---

## 🧑‍🦯 Guida passo-passo per usare l'app Meteo (anche se non sei uno sviluppatore)

Questa sezione è pensata per chi vuole **usare l'app meteo in locale** senza conoscere nulla di programmazione, Java o Docker.

> Segui questi passaggi uno alla volta e sarai operativa in pochi minuti 🎯

---

### 🔧 Cosa ti serve installato sul PC

Assicurati di avere:

1. ✅ **[Java 17+](https://adoptium.net/temurin/releases/?version=17)**  
2. ✅ **[Docker Desktop](https://www.docker.com/products/docker-desktop)**  

---

### 📦 1. Scarica il progetto

Se hai Git:

```bash
git clone https://github.com/valentina-its/meteo-app.git
```
```bash
cd meteo-app
```
Altrimenti scarica il progetto come ZIP da GitHub e estrailo dove vuoi.

## 🔨 2. Compila il progetto

Apri il terminale (o PowerShell su Windows), entra nella cartella del progetto e scrivi:

```bash
./mvnw clean package -DskipTests
```

Questo comando crea il file .jar pronto per essere messo nel contenitore Docker.

## 🐳 3. Crea l’immagine Docker

```bash
docker build -t meteo-app .
```

🧊 Docker userà il Dockerfile incluso per costruire l’ambiente completo.

## ♻️ 4. (Facoltativo) Rimuovi un vecchio container

Se l’hai già avviato in passato:

```bash
docker rm -f meteo
```

🧽 Così eviti errori tipo “il container esiste già”.

## 🚀 5. Avvia l’app
```bash
docker run -p 8080:8080 --name meteo meteo-app
```

Lascia il terminale aperto: l’app è attiva!

## 🌐 6. Apri l'applicazione

Vai nel browser all’indirizzo:
```bash
http://localhost:8080
```

Vedrai:
- Un menu a tendina con le città (Torino, Milano, ecc)
- Un bottone per “Mostra grafico”
- Un grafico meteo aggiornato in tempo reale 📈
