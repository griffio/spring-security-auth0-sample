# Auth0 + Spring API Seed

## Spring Boot, Spring Security Java Config

Updated Gradle project with Spring Boot, Spring Security Java Config and JWT (nimbus-jose-jwt) library.

https://auth0.com/ 

This version is uses a simple JWT as the bearer token.

The initial JWT is produced by a successful authentication with auth0.

The Resource Server acts as a simple turn-style granting access when a valid token is presented in the Authorisation header. 

#Running the example

You need to create an Auth0 application with a valid database user account to login via the http://localhost:8080/index.html page.

Change the setupPage values in index.html to your application domain and client id provided by Auth0 (apps/APIs).

~~~javascript
   function setupPage() {
        var domain = 'FIXME';
        var cid = 'FIXME';
        ...
   }
~~~

~~~
npm init
~~~

Browserify the client
~~~
npm run bundle
~~~

The ClientSecret, ClientId and Domain for Auth0 app are not supplied and are provided from Auth0 (Apps/APIs).

You can set these variables in the `application.properties` file, or you can set them as arguments when running the app.

Gradle processes use -P properties that can then pass arguments in the build script to the JavaExec process.

Spring Boot plugin runner
~~~
./gradlew bootRun -Pauth0="--auth0.clientId=FIXME,--auth0.clientSecret=FIXME"
~~~

No auth
~~~
curl -v http://localhost:8080/handshake
~~~

Auth required
~~~
curl -v --header "Authorization: Bearer <Auth0 id token>" http://localhost:8080/authorised/handshake
~~~
