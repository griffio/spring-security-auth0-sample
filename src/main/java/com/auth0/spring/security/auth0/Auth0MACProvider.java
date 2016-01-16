package com.auth0.spring.security.auth0;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

public class Auth0MACProvider {

  private static final Logger logger = LoggerFactory.getLogger(Auth0MACProvider.class);

  private final MACVerifier macVerifier;

  public Auth0MACProvider(MACVerifier macVerifier) {
    this.macVerifier = macVerifier;
  }

  public boolean isVerified(SignedJWT jwt) {

    logger.info(macVerifier.getSecretString());

    try {
      return jwt.verify(macVerifier);
    } catch (JOSEException e) {
      throwCheckedException(e);
    }
    return false;
  }

  public JWTClaimsSet extractClaims(SignedJWT signedJWT) {

    try {
      if (signedJWT.verify(macVerifier)) {
        return signedJWT.getJWTClaimsSet();
      }
    } catch (ParseException | JOSEException e) {
      throwCheckedException(e);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private <T extends Throwable> void throwCheckedException(Throwable t) throws T {
    throw (T) t;
  }
}
