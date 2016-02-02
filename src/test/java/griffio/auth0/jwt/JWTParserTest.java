package griffio.auth0.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.Key;

public class JWTParserTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  Claims expected;
  String signedJWT;
  Key secret;
  Key bogusSecret;

  private static Claims claims() {
    return Jwts.claims()
        .setIssuer("griff.io")
        .setSubject("test.work")
        .setAudience("localhost");
  }

  @Before
  public void fixture() throws Exception {
    secret = MacProvider.generateKey();
    bogusSecret = MacProvider.generateKey();
    expected = claims();
    signedJWT = Jwts.builder().setClaims(expected).signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  @Test
  public void jwt_bogus_secret_key_claims_are_not_valid() throws Exception {
    thrown.expect(SignatureException.class);
    Jwts.parser().setSigningKey(bogusSecret).parseClaimsJws(signedJWT);
  }

  @Test
  public void jwt_secret_key_claims_verified() throws Exception {
    Jws<Claims> actual = Jwts.parser().setSigningKey(secret).parseClaimsJws(signedJWT);
    Assert.assertEquals(expected.getIssuer(), actual.getBody().getIssuer());
    Assert.assertEquals(expected.getSubject(), actual.getBody().getSubject());
    Assert.assertEquals(expected.getAudience(), actual.getBody().getAudience());
  }

}