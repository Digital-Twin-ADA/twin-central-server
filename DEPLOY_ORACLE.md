# Deploy Central Server to Oracle Cloud Infrastructure

This deployment runs the Spring Boot central server in Docker on an Oracle Cloud Infrastructure Always Free VM. The app can keep using Neon PostgreSQL as its database.

## 1. Local checks

From this folder:

```powershell
mvn test
mvn clean package -DskipTests
docker build -t central-server:latest .
```

## 2. Create an Oracle Cloud account

Create or sign in to Oracle Cloud:

```text
https://www.oracle.com/cloud/free/
```

Use the Free Tier / Always Free option. Oracle may ask for phone and card verification. Oracle says Always Free resources do not expire, but paid/trial resources outside the free limits can cost money.

## 3. Create an Always Free VM

In Oracle Cloud Console:

1. Open the navigation menu.
2. Go to `Compute` > `Instances`.
3. Click `Create instance`.
4. Name: `central-server`.
5. Image: `Oracle Linux 9` or `Oracle Linux 8`.
6. Shape: choose an Always Free eligible shape:
   - `VM.Standard.A1.Flex` with 1 OCPU and 6 GB RAM, recommended if available.
   - Or `VM.Standard.E2.1.Micro` if A1 capacity is unavailable.
7. Networking:
   - Use a public subnet.
   - Make sure `Assign public IPv4 address` is enabled.
8. SSH keys:
   - Upload your public SSH key, or let Oracle generate/download keys.
9. Click `Create`.

Save the VM public IP address.

## 4. Open the app port in Oracle networking

For first deployment, open TCP port `8080`.

In Oracle Cloud Console:

1. Go to the VM details page.
2. Click the linked `Virtual cloud network`.
3. Open the public subnet.
4. Open the subnet security list.
5. Add an ingress rule:
   - Source CIDR: `0.0.0.0/0`
   - IP Protocol: `TCP`
   - Destination port range: `8080`

Later, use `80`/`443` with a reverse proxy and HTTPS.

## 5. Connect to the VM

From your local terminal:

```powershell
ssh opc@<VM_PUBLIC_IP>
```

If Oracle generated a private key for you:

```powershell
ssh -i .\path\to\private.key opc@<VM_PUBLIC_IP>
```

## 6. Install Docker on the VM

Run on the Oracle VM:

```bash
sudo dnf update -y
sudo dnf install -y dnf-utils zip unzip git
sudo dnf config-manager --add-repo=https://download.docker.com/linux/centos/docker-ce.repo
sudo dnf install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
sudo systemctl enable --now docker
sudo usermod -aG docker opc
```

Then disconnect and reconnect:

```bash
exit
ssh opc@<VM_PUBLIC_IP>
```

Check Docker:

```bash
docker version
```

## 7. Copy the project to the VM

From your local machine, run this from the parent folder that contains `central-server`:

```powershell
scp -r .\central-server opc@<VM_PUBLIC_IP>:/home/opc/central-server
```

If using a private key:

```powershell
scp -i .\path\to\private.key -r .\central-server opc@<VM_PUBLIC_IP>:/home/opc/central-server
```

## 8. Build the Docker image on the VM

Run on the Oracle VM:

```bash
cd /home/opc/central-server
docker build -t central-server:latest .
```

## 9. Run the server

Set these values using your Neon database connection details. Rotate the Neon password first if it was ever committed or shared.

```bash
docker rm -f central-server || true

docker run -d \
  --name central-server \
  --restart unless-stopped \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://<host>/<database>?sslmode=require" \
  -e SPRING_DATASOURCE_USERNAME="<database-user>" \
  -e SPRING_DATASOURCE_PASSWORD="<database-password>" \
  -e SPRING_JPA_SHOW_SQL="false" \
  -e SPRING_JPA_FORMAT_SQL="false" \
  central-server:latest
```

Check logs:

```bash
docker logs -f central-server
```

## 10. Test the public API

From your local machine:

```powershell
Invoke-RestMethod "http://<VM_PUBLIC_IP>:8080/api/stages"
```

If that returns JSON, send this to your colleague:

```text
Central server URL: http://<VM_PUBLIC_IP>:8080

Current endpoints:
GET  /api/stages
GET  /api/stages/{id}
POST /api/stages
POST /api/telemetry
```

## 11. Useful VM commands

Restart:

```bash
docker restart central-server
```

Stop:

```bash
docker stop central-server
```

View logs:

```bash
docker logs -f central-server
```
