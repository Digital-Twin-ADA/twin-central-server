# Deploy Central Server to Azure App Service

This project is a Spring Boot executable JAR. Deploy it to Azure App Service as a Linux Java SE app using Java 21.

## 1. Prepare the database values

Do not commit database credentials. Put them in local environment variables first:

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://<host>/<database>?sslmode=require"
$env:SPRING_DATASOURCE_USERNAME="<database-user>"
$env:SPRING_DATASOURCE_PASSWORD="<database-password>"
```

If the old database password was committed or shared, rotate it in the database provider before deploying.

## 2. Check the app still builds locally

From this folder:

```powershell
mvn clean package -DskipTests
```

The JAR should be created at:

```text
target/central-server-0.0.1-SNAPSHOT.jar
```

## 3. Create Azure resources

Pick an app name that is globally unique. The final public URL will be `https://<app-name>.azurewebsites.net`.

```powershell
az login

$RESOURCE_GROUP="ada-central-rg"
$PLAN_NAME="ada-central-plan"
$APP_NAME="ada-central-server-<your-name-or-team>"
$LOCATION="francecentral"

az group create `
  --name $RESOURCE_GROUP `
  --location $LOCATION

az appservice plan create `
  --name $PLAN_NAME `
  --resource-group $RESOURCE_GROUP `
  --sku F1 `
  --is-linux

az webapp create `
  --resource-group $RESOURCE_GROUP `
  --plan $PLAN_NAME `
  --name $APP_NAME `
  --runtime "JAVA:21-java21" `
  --https-only true
```

`F1` is the free tier. It is suitable for testing, not production.

## 4. Configure startup and environment variables

```powershell
az webapp config set `
  --resource-group $RESOURCE_GROUP `
  --name $APP_NAME `
  --startup-file "java -jar /home/site/wwwroot/app.jar --server.port=80"

az webapp config appsettings set `
  --resource-group $RESOURCE_GROUP `
  --name $APP_NAME `
  --settings `
    SPRING_DATASOURCE_URL="$env:SPRING_DATASOURCE_URL" `
    SPRING_DATASOURCE_USERNAME="$env:SPRING_DATASOURCE_USERNAME" `
    SPRING_DATASOURCE_PASSWORD="$env:SPRING_DATASOURCE_PASSWORD" `
    SPRING_JPA_SHOW_SQL="false" `
    SPRING_JPA_FORMAT_SQL="false"
```

## 5. Deploy the JAR

```powershell
az webapp deploy `
  --resource-group $RESOURCE_GROUP `
  --name $APP_NAME `
  --src-path .\target\central-server-0.0.1-SNAPSHOT.jar `
  --type jar
```

## 6. Verify the server

```powershell
$BASE_URL="https://$APP_NAME.azurewebsites.net"

Invoke-RestMethod "$BASE_URL/api/stages"
```

If it responds, send this base URL to the team:

```text
https://<app-name>.azurewebsites.net
```

Current API routes include:

```text
GET  /api/stages
GET  /api/stages/{id}
POST /api/stages
POST /api/telemetry
```

## 7. Useful commands

Tail logs:

```powershell
az webapp log tail --resource-group $RESOURCE_GROUP --name $APP_NAME
```

Stop the app when not testing:

```powershell
az webapp stop --resource-group $RESOURCE_GROUP --name $APP_NAME
```

Start it again:

```powershell
az webapp start --resource-group $RESOURCE_GROUP --name $APP_NAME
```
