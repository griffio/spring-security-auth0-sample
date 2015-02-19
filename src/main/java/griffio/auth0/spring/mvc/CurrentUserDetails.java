package griffio.auth0.spring.mvc;

public class CurrentUserDetails {

    private final String username;

    public CurrentUserDetails(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "ApplicationUser{" +
                "username='" + username + '\'' +
                '}';
    }

    public static CurrentUserDetails fromUsername(String username) {
        return new CurrentUserDetails(username);
    }

}
