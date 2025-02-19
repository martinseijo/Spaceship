# Usa la imagen oficial de Eclipse Temurin con JDK 21 y Alpine
FROM eclipse-temurin:21-jdk-alpine AS builder

# Instala Maven en la imagen
RUN apk add --no-cache maven

# Establece el directorio de trabajo para el build
WORKDIR /app

# Copia el archivo pom.xml y descarga las dependencias del proyecto
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el código fuente y compila el proyecto
COPY src ./src
RUN mvn package -DskipTests

# Usa la imagen oficial de OpenJDK para ejecutar la aplicación
FROM eclipse-temurin:21-jdk-alpine

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el JAR compilado desde la fase de build
COPY --from=builder /app/target/spaceship-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en el que se ejecutará la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación con el perfil prod
ENTRYPOINT ["java", "-jar", "app.jar"]
