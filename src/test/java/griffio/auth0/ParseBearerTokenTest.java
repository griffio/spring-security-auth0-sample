package griffio.auth0;

import com.auth0.spring.security.auth0.Auth0BearerAuthentication;
import org.junit.Assert;
import org.junit.Test;

public class ParseBearerTokenTest {

  @Test
  public void parse_success() throws Exception {

    final String bearerToken = "Bearer 123456 ";

    Auth0BearerAuthentication bearerValue = Auth0BearerAuthentication.create(bearerToken);

    Assert.assertEquals("123456", bearerValue.getName());

  }

}
