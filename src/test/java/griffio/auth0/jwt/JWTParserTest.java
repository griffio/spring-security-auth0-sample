package griffio.auth0.jwt;

import com.auth0.spring.security.auth0.Auth0JWTVerifyHandler;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JWTParserTest {

  static final StringBuilder secretKeySource = new StringBuilder("12345678901234567890123456789012");

  ReadOnlyJWTClaimsSet expected;

  private static ReadOnlyJWTClaimsSet claimsSet() {
    JWTClaimsSet claimsSet = new JWTClaimsSet();
    claimsSet.setIssuer("griff.io");
    claimsSet.setSubject("test.work");
    claimsSet.setAudience("localhost");
    return claimsSet;
  }

  @Before
  public void fixture() throws Exception {
    expected = claimsSet();
  }

  @Test
  public void jwt_reversed_secret_key_claims_are_null() throws Exception {

    SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), expected);
    signedJWT.sign(new MACSigner(secretKeySource.toString()));
    Auth0JWTVerifyHandler parseHandler = new Auth0JWTVerifyHandler(secretKeySource.reverse().toString());
    Assert.assertNull(JWTParser.parse(signedJWT.serialize(), parseHandler));

  }

  @Test
  public void jwt_secret_key_claims_verified() throws Exception {

    SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), expected);
    signedJWT.sign(new MACSigner(secretKeySource.toString()));
    Auth0JWTVerifyHandler parseHandler = new Auth0JWTVerifyHandler(secretKeySource.toString());
    ReadOnlyJWTClaimsSet actual = JWTParser.parse(signedJWT.serialize(), parseHandler);

    Assert.assertEquals(expected.getIssuer(), actual.getIssuer());
    Assert.assertEquals(expected.getSubject(), actual.getSubject());
    Assert.assertEquals(expected.getAudience(), actual.getAudience());

  }

}