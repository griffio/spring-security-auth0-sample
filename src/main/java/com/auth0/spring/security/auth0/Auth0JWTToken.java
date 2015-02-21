package com.auth0.spring.security.auth0;

import java.text.ParseException;
import java.util.Collection;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

/**
 * Implements the org.springframework.security.core.Authentication interface.
 * The constructor is set with the Auth0 JWT
 *
 * @author Daniel Teixeira
 */
public class Auth0JWTToken extends AbstractAuthenticationToken {

  private static final long serialVersionUID = 2371882820082543721L;
  private final JWT jwt;
  private Auth0UserDetails principal;

  public Auth0JWTToken(String jwt) throws AuthenticationException {
    super(null);
    try {
      this.jwt = JWTParser.parse(jwt);
    } catch (ParseException e) {
      throw new Auth0TokenException(e.getMessage());
    }
    setAuthenticated(false);
  }

  public JWT getJwt() {
    return jwt;
  }

  public Object getCredentials() {
    return null;
  }

  public Object getPrincipal() {
    return principal;
  }

  public void setPrincipal(Auth0UserDetails principal) {
    this.principal = principal;
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return principal.getAuthorities();
  }

}
