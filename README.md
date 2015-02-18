# Auth0 + Spring API Seed

Updated Gradle project with Spring Boot, Spring Security Java Config and JWT library.

https://auth0.com/ 

This version is uses a simple JWT as the bearer token.

The initial JWT is produced by a successful authentication with auth0.

The Resource Server acts as a simple turn-style granting access when a valid token is presented in the Authorisation header. 

#Running the example

The ClientSecret, ClientId and Domain for Auth0 app is not supplied.

You can set these variables in the `auth0.properties` file, or you can set them as arguments when running the app (with `-Dauth0.clientSecret=secret -Dauth0.clientId=clientid -Dauth0.domain=yourdomain.auth0.com`)

```
./gradlew jettyRun

curl -v http://localhost:8080/spring-security-auth0-api-example/handshake
```
