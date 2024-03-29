#######################
# Docker image to BUILD
FROM openjdk:17.0.1-oraclelinux7 as build
WORKDIR /workspace/app

# copy maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
# install all dependencies so that they get cached
RUN ./mvnw dependency:go-offline -B

# copy source
COPY src src

# build
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

######################################
# Docker image to RUN (based on build)
FROM openjdk:17.0.1-oraclelinux7
VOLUME /tmp
# copy files from build
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","be.kuleuven.cs.distrinet.gmsa.deltaiot.Server"]
EXPOSE 8080
