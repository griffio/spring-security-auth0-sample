# Auth0 + Spring API Seed

## Spring Boot, Spring Security Java Config

Updated Gradle project with Spring Boot, Spring Security Java Config and JWT library.

https://auth0.com/ 

This version is uses a simple JWT as the bearer token.

The initial JWT is produced by a successful authentication with auth0.

The Resource Server acts as a simple turn-style granting access when a valid token is presented in the Authorisation header. 

#Running the example

~~~
npm init
~~~

Browserify the client
~~~
npm run bundle
~~~

The ClientSecret, ClientId and Domain for Auth0 app is not supplied.

You can set these variables in the `application.properties` file, or you can set them as arguments when running the app.

Gradle processes use -P properties that can then pass arguments in the build script to the JavaExec process.

Spring Boot plugin runner
~~~
./gradlew bootRun -Pauth0="--auth0.clientId=FZ7Acusjd1BEjf4nbdid6x9PTJLBrE8P,--auth0.clientSecret=FIXME"
~~~

No auth
~~~
curl -v http://localhost:8080/handshake
~~~

Auth required
~~~
curl -v --header "Authorization: Bearer <Auth0 id token>" http://localhost:8080/authorised/handshake
~~~
