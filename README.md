# Auth0 + Spring API Seed

## Spring Boot, Spring Security Java Config with Java 8

Updated Gradle project with Spring Boot, Spring Security Java Config and JWT (nimbus-jose-jwt) library.

https://auth0.com/ 

This version is uses a simple JWT as the bearer token.

The initial JWT is produced by a successful authentication with auth0.

Example encoded Json web token format (see http://jwt.io/) to debug.
~~~
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2dyaWZmaW8tYXBwbGljYXRpb24uYXV0aDAuY29tLyIsInN1YiI6ImF1dGgwfDU0YzkzMmJhYTUxNzAzYWUwNGMxMjQyMCIsImF1ZCI6IkZaN0FjdXNqZDFCRWpmNG5iZGlkNng5UFRKTEJyRThQIiwiZXhwIjoxNDI2OTgyNjQ5LCJpYXQiOjE0MjY5NDY2NDl9.o1eJwoC69jC9_hNePGts9vHUR79YiSS_hZybFQ1weeU
~~~

~~~json
{
 "iss": "https://example.auth0.com/",
 "sub": "auth0|11c111baa1111ae01c11111",
 "aud": "TheClientId",
 "exp": 1426964798,
 "iat": 1426928798
 }
~~~

The Resource Server acts as a simple turn-style granting access when a valid token is presented in the Authorisation header. 

#Running the example

You need to create an Auth0 application with a valid database user account to login via the http://localhost:8080/index.html page.

Change the setupPage values in index.html to your application domain and client id provided by Auth0 (apps/APIs).

The javascript client requests the UserInfo resource from auth0.

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
./gradlew bootRun -Pauth0="--auth0.clientId=FIXME,--auth0.clientSecret=FIXME,--auth0.domain=FIXME"
~~~

Browser page http://localhost:8080/
Should should auth0 login page

No auth
~~~
curl -v http://localhost:8080/handshake
~~~

Auth required
~~~
curl -v --header "Authorization: Bearer <Auth0 id token>" http://localhost:8080/authorised/handshake
~~~
