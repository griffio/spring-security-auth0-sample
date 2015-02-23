package com.auth0.spring.security.auth0;

import org.springframework.security.core.AuthenticationException;

public class Auth0TokenException extends AuthenticationException {

  private static final long serialVersionUID = 1L;

  public Auth0TokenException(String msg) {
    super(msg);
  }

  public Auth0TokenException(String msg, Throwable t) {
    super(msg, t);
  }

  public Auth0TokenException(Exception e) {
    super(e.getMessage(), e);
  }

}
