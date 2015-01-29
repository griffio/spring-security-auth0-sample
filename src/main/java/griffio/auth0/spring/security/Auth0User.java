package griffio.auth0.spring.security;

public class Auth0User {

    private final String username;

    public Auth0User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "Auth0User{" +
                "username='" + username + '\'' +
                '}';
    }

    public static Auth0User fromUsername(String username) {
        return new Auth0User(username);
    }

}
