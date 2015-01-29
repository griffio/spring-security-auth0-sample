# Auth0 + Spring API Seed

Updated Project with Spring Boot and Gradle and JWT library.

#Running the example

The ClientSecret, ClientId and Domain for Auth0 app is not supplied.

You can set these variables in the `auth0.properties` file, or you can set them as arguments when running the app (with `-Dauth0.clientSecret=secret -Dauth0.clientId=clientid -Dauth0.domain=yourdomain.auth0.com`)

```
./gradlew jettyRun

curl -v http://localhost:8080/spring-security-auth0-api-example/handshake
```
