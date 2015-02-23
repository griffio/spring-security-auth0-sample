package griffio.auth0.spring.mvc;

public class UserAccount {

  private final String username;
  private final String email;

  public UserAccount(String username, String email) {
    this.username = username;
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  @Override
  public String toString() {
    return "UserAccount{" +
        "username='" + username + '\'' +
        ", email='" + email + '\'' +
        '}';
  }

  public static UserAccount create(String username, String email) {
    return new UserAccount(username, email);
  }

}
