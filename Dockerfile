FROM openjdk:11.0.12-jdk-slim-bullseye as builder
ARG BUILD_CMD="./mvnw clean package -DskipTests -Dmaven.test.skip=true"
COPY .mvn .mvn
COPY mvnw .
#download mvn in a separate layer
RUN ./mvnw ./mvnw --version \
    && rm /root/.m2/wrapper/dists/apache-maven-3.8.3-bin/5a6n1u8or3307vo2u2jgmkhm0t/apache-maven-3.8.3-bin.zip
COPY pom.xml .
#download all dependencies in a separate layer. We do not want to download them everytime source changes
RUN ${BUILD_CMD} -Dspring-boot.repackage.skip=true \
    && ./mvnw clean
COPY src/ src/
RUN ${BUILD_CMD} \
    && mkdir build \
    && (cd build; jar -xf ../target/*.jar) \
    && ./mvnw clean

FROM openjdk:11.0.12-jre-slim-bullseye
ARG DEPENDENCY=/build
WORKDIR /app
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib lib
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes .
CMD ["java", "-verbose:gc", "-cp", "/app:/app/lib/*", "pl.krejnstudio.smarttools.coldbedroomnotifier.ColdBedroomNotifierApplication"]