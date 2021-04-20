FROM openjdk:16-jdk-slim as build
WORKDIR /account-manager/build

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:16-slim as run

RUN groupadd -r usergroup123 && useradd -r -s /bin/false -g usergroup123 user123
USER user123

WORKDIR /account-manager

ARG DEPENDENCY=/account-manager/build/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib ./lib
COPY --from=build ${DEPENDENCY}/META-INF ./META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes .

EXPOSE 8080
ENTRYPOINT ["java","-cp","./:./lib/*","com.myowncompany.account.manager.AccountManagerApplication"]