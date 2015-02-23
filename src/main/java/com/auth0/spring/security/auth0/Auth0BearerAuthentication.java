package com.auth0.spring.security.auth0;

import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;

import java.text.ParseException;

public class Auth0BearerAuthentication extends PreAuthenticatedAuthenticationToken {

  private static final long serialVersionUID = 1L;

  public Auth0BearerAuthentication(String principal) {
    super(principal, "");
  }

  public static Auth0BearerAuthentication create(String bearer) throws ParseException {

    if (StringUtils.isEmpty(bearer)) {
      throw new ParseException("Bearer token expected", 0);
    }

    String[] parts = bearer.split("\\s", 2);

    if (!parts[0].equals("Bearer")) {
      throw new ParseException("Token type must be Bearer", 0);
    }

    if (parts.length != 2) {
      throw new ParseException("Bearer requires value", "Bearer".length());
    }

    return new Auth0BearerAuthentication(parts[1].trim());

  }

}
