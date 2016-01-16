package griffio.auth0.jwt;

import com.auth0.spring.security.auth0.Auth0MACProvider;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JWTParserTest {

  JWTClaimsSet expected;
  MACVerifier bogusMacVerifier;
  MACVerifier macVerifier;
  MACSigner macSigner;
  StringBuilder secretKeySource;

  private static JWTClaimsSet claimsSet() {
    JWTClaimsSet.Builder claimsSet = new JWTClaimsSet.Builder();
    claimsSet.issuer("griff.io");
    claimsSet.subject("test.work");
    claimsSet.audience("localhost");
    return claimsSet.build();
  }

  @Before
  public void fixture() throws Exception {
    secretKeySource = new StringBuilder("12345678901234567890123456789012");
    macSigner = new MACSigner(secretKeySource.toString());
    macVerifier = new MACVerifier(secretKeySource.toString());
    bogusMacVerifier = new MACVerifier(secretKeySource.reverse().toString());
    expected = claimsSet();
  }

  @Test
  public void jwt_reversed_secret_key_claims_are_not_valid() throws Exception {
    SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), expected);
    signedJWT.sign(macSigner);
    Auth0MACProvider auth0MACProvider = new Auth0MACProvider(bogusMacVerifier);
    Assert.assertNull(auth0MACProvider.extractClaims(signedJWT));
  }

  @Test
  public void jwt_secret_key_claims_verified() throws Exception {
    SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), expected);
    signedJWT.sign(macSigner);
    Auth0MACProvider auth0MACProvider = new Auth0MACProvider(macVerifier);
    JWTClaimsSet actual = auth0MACProvider.extractClaims(signedJWT);
    Assert.assertEquals(expected.getIssuer(), actual.getIssuer());
    Assert.assertEquals(expected.getSubject(), actual.getSubject());
    Assert.assertEquals(expected.getAudience(), actual.getAudience());
  }

}