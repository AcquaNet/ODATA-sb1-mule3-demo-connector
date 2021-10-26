# SB1 Connector


## Instalar SB1 Connector


Descargar Conector e Instalarlo en AnypointStudio

[Download latest SB1 Mule 3 Connector from Repository unpkg.com](http://157.245.236.175:8081/artifactory/libs-release/com/atina/SB1Mule3Connector/1.0.0/SB1Mule3Connector-1.0.0.zip).


## Instalar SB1 Microservice


### Descargar Docker Composer

Crear una carpeta en el disco y descargar los siguientes archivos.

[.env](https://github.com/AcquaNet/SB1Docker/blob/master/ODATADockerDistribution/.env).

[.docker-compose](https://github.com/AcquaNet/SB1Docker/blob/master/ODATADockerDistribution/docker-compose.yml).


### Ejecutar Docker

Desde la carperta que se creo en el punto anterior ejecutar el siguiente comnando

```bash
docker-compose up --no-start
```

```bash
docker-compose start
```

Verificar que este corriendo correctamente

```bash
docker logs odata-atina-microserver
```

Debe mostrar que el servicio este funcionando correctamente:


```log
 c.a.j.j.server.JDERestServer - ------------------------------------------------------
 c.a.j.j.server.JDERestServer - Configuracion:
 c.a.j.j.server.JDERestServer -        SERVICIO:
 c.a.j.j.server.JDERestServer -           IP del Servicio = [172.47.0.7]
 c.a.j.j.server.JDERestServer -           Puerto del Servicio = [8085]
 c.a.j.j.server.JDERestServer -           IP local del Servicio = [0.0.0.0]
 c.a.j.j.server.JDERestServer -           Expiracion = [3000000]
 c.a.j.j.server.JDERestServer -           Last Build = [2020-11-17 15:51]
 c.a.j.j.server.JDERestServer - *-------------------------------------*
 c.a.j.j.server.JDERestServer - *   Starting SB1 Microservice 1.0.0   *
 c.a.j.j.server.JDERestServer - *   SB1 Microservice started!         *
 c.a.j.j.server.JDERestServer - *-------------------------------------*
```


Adicionales

Se puede consultar el proceso de instalacion con el siguiente comando

```bash
docker exec -it  odata-atina-microserver cat /tmp/start.log
```

Listar logs generados

```bash
docker exec -it  odata-atina-microserver ls -l /tmp/odata/ODATAConnectorServerLog
```











