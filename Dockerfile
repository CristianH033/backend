FROM eclipse-temurin:22-jdk-jammy

WORKDIR /app

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Copiar los archivos de Maven
# COPY mvnw pom.xml ./
# COPY .mvn .mvn

# Descargar las dependencias
# RUN ./mvnw dependency:go-offline

# Copiar el código fuente
# COPY src ./src

# Compilar la aplicación
# RUN ./mvnw package -DskipTests

# CMD ["java", "-jar", "target/api-0.0.1-SNAPSHOT.jar"]

# Avoid terminating the container
CMD [ "tail", "-f", "/dev/null" ]