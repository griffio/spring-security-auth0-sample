package com.auth0.spring.security.auth0;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTHandlerAdapter;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public class Auth0JWTVerifyHandler extends JWTHandlerAdapter<ReadOnlyJWTClaimsSet> {

  private final MACVerifier macVerifier;

  public Auth0JWTVerifyHandler(String secretKey) {
    macVerifier = new MACVerifier(secretKey);
  }

  @Override
  public ReadOnlyJWTClaimsSet onSignedJWT(SignedJWT signedJWT) {
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
