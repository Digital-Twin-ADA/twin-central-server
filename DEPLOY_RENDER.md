# Deploy Central Server to Render

Render is usually the fastest way to get a public backend URL for testing. It can build this project directly from GitHub using the Dockerfile.

## 1. Before deploying

Make sure the app passes locally:

```powershell
mvn test
mvn clean package -DskipTests
```

Rotate your Neon database password if it was ever committed or shared.

## 2. Push the project to GitHub

Render works best when it can connect to your GitHub repository and auto-deploy after each push.

Commit these deployment files:

```text
Dockerfile
.dockerignore
src/main/resources/application.yaml
src/test/resources/application.yaml
pom.xml
```

## 3. Create the Render Web Service

1. Go to:

```text
https://dashboard.render.com
```

2. Click `New` > `Web Service`.
3. Connect your GitHub repository.
4. Choose the branch you want to deploy, usually `main`.
5. Set:

```text
Name: central-server
Language: Docker
Region: Frankfurt, if available near Romania, otherwise any available free region
Instance type: Free, for testing
```

Render will detect the `Dockerfile`.

## 4. Add environment variables

In the Render service creation screen, open `Environment Variables` and add:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>/<database>?sslmode=require
SPRING_DATASOURCE_USERNAME=<database-user>
SPRING_DATASOURCE_PASSWORD=<database-password>
SPRING_JPA_SHOW_SQL=false
SPRING_JPA_FORMAT_SQL=false
```

Do not add quotes around the values in the Render UI.

## 5. Deploy

Click `Create Web Service`.

Render will:

1. Clone the GitHub repo.
2. Build the Docker image.
3. Run the Spring Boot server.
4. Give you a public URL like:

```text
https://central-server.onrender.com
```

## 6. Test

Open:

```text
https://central-server.onrender.com/api/stages
```

Or from PowerShell:

```powershell
Invoke-RestMethod "https://central-server.onrender.com/api/stages"
```

If it responds, send your colleague:

```text
Central server URL: https://central-server.onrender.com

Current endpoints:
GET  /api/stages
GET  /api/stages/{id}
POST /api/stages
POST /api/telemetry
```

## Notes

Render Free web services can spin down when idle, so the first request after inactivity can be slow. This is acceptable for demos and testing, but not ideal for production.
