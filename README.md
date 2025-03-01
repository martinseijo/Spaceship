# Spaceship Project

## Levantar el proyecto

### Con Docker Compose
Para construir y levantar el proyecto con Docker Compose, ejecuta los siguientes comandos:
```bash
docker-compose build
docker-compose up
```

### Con Docker 
Para construir y levantar el proyecto con Docker, ejecuta los siguientes comandos:
```bash
docker build -t spaceship .
docker run -p 8080:8080 spaceship
```

### En local
Para levantar el proyecto en local, asegúrate de tener Maven 3.9.9 y JDK 21 instalados. Luego, ejecuta:
```bash
mvn clean install
mvn spring-boot:run
```

## Swagger
La interfaz de Swagger está disponible en la siguiente ruta: http://localhost:8080/swagger-ui/index.html

## Consumo de Endpoints

#### Obtener todas las naves espaciales
    GET /spaceships

#### Obtener naves espaciales paginadas
    GET /spaceships/paginated?page={page}&size={size}

#### Obtener nave espacial por ID
    GET /spaceships/{id}

#### Buscar naves espaciales por filtro
    POST /spaceships/search

    Content-Type: application/json
    
    {
    "name": "Enterprise"
    }

#### Crear una nueva nave espacial
    POST /spaceships/create
    Content-Type: application/json
    
    {
    "name": "New Spaceship"
    }

#### Actualizar una nave espacial existente
    PUT /spaceships/update
    Content-Type: application/json
    
    {
    "id": 1,
    "name": "Updated Spaceship"
    }
#### Eliminar una nave espacial por ID
    DELETE /spaceships/delete/{id}