# Description

Collects measurements from temperature sensors. Stores data in monogodb as collection. 
Processes incoming data respectively. Sends data to websocket channel, so that clients can
update their state respectively. Provides simple configuration tool for administrative tasks.

# Environment variables

```shell
MONGO_ROOT_USERNAME
MONGO_ROOT_PASSWORD
REGULAR_USER
REGULAR_USER_PASSWORD
ADMIN_USER
ADMIN_USER_PASSWORD
MONGO_HOST
SENSOR_USER
SENSOR_USER_PASSWORD
MONGO_RESTART
MONGO_EXPRESS_RESTART
CBN_RESTART
```

# Starting

Needs docker and docker-compose installed.

```shell
$ ./startservice.sh
```

with that, after build process is finished, service is available at:

```shell
http://localhost:8080/
```

# Sample REST endpoints requests:

## Sending temperature measurement

```shell
curl --location --request POST 'http://localhost:8080/api/temperature' \
--header 'Authorization: Basic c2Vuc29yOnNlbnNvcg==' \
--header 'Content-Type: application/json' \
--data-raw '{
    "sensor":"bedroom",
    "value": 20.44
}'

```