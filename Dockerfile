FROM eclipse-temurin:22-jdk-jammy

WORKDIR /app

# Instalar Maven
RUN apt-get update && apt-get install -y maven git

# Crear un usuario no root
RUN useradd -ms /bin/bash devuser

# Establecer la propiedad del directorio /app al usuario no root
RUN chown -R devuser:devuser /app

# Cambiar al usuario no root
USER devuser

# Copiar el archivo POM
COPY pom.xml /app

# Copiar el código fuente
COPY src ./src

# Descargar las dependencias
RUN mvn go-offline:resolve-dependencies

# Compilar la aplicación
RUN mvn package --offline -DskipTests -Dmaven.test.skip=true

# CMD ["java", "-jar", "target/api-0.0.1-SNAPSHOT.jar"]

# Avoid terminating the container
CMD [ "tail", "-f", "/dev/null" ]