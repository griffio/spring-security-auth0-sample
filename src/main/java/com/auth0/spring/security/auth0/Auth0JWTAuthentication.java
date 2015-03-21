package com.auth0.spring.security.auth0;

import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
/**
 The claims provide the following attributes (example values).
 {
 "iss": "https://example.auth0.com/",
 "sub": "auth0|11c111baa1111ae01c11111",
 "aud": "TheClientId",
 "exp": 1426964798,
 "iat": 1426928798
 }
 */
public class Auth0JWTAuthentication extends AbstractAuthenticationToken {

  private static final long serialVersionUID = 1L;

  private Object principal;
  private Object credentials;

  @Override
  public Object getCredentials() {
    return credentials;
  }

  public Auth0JWTAuthentication(ReadOnlyJWTClaimsSet claimsSet) {
    super(AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    this.principal = String.format("subject:%s@%s", claimsSet.getSubject(), claimsSet.getIssuer());
    this.credentials = claimsSet;
    setAuthenticated(true);
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }

  public static Auth0JWTAuthentication create(ReadOnlyJWTClaimsSet claimsSet) {
    return new Auth0JWTAuthentication(claimsSet);
  }

}