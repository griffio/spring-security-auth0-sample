package com.auth0.spring.security.auth0;

import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class Auth0JWTAuthentication extends AbstractAuthenticationToken {

  private static final long serialVersionUID = 1L;

  private Object principal;
  private Object credentials;

  public Auth0JWTAuthentication(UserDetails userDetails, ReadOnlyJWTClaimsSet claimsSet) {
    super(userDetails.getAuthorities());
    this.principal = userDetails;
    this.credentials = claimsSet;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return this.credentials;
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }

  public static Auth0JWTAuthentication create(ReadOnlyJWTClaimsSet claimsSet) {
    User user = new User(claimsSet.getSubject(), "", AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));

    return new Auth0JWTAuthentication(user, claimsSet);
  }

}