docker-compose --env-file .env down
docker rm -fv cbn-server_cbn_1
docker-compose build cbn
docker-compose --env-file .env up -d
