package com.auth0.spring.security.auth0;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class Auth0AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  public Auth0AuthenticationFilter() {
    super(AnyRequestMatcher.INSTANCE);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

    String authorizationHeader = request.getHeader("Authorization");

    if (StringUtils.isEmpty(authorizationHeader)) {
      throw new Auth0TokenException("Authorization header missing from request. Expected Authorization:");
    }

    Auth0BearerAuthentication bearerAuthentication;

    try {
      bearerAuthentication = Auth0BearerAuthentication.create(authorizationHeader);
    } catch (ParseException e) {
      throw new Auth0TokenException(e);
    }

    return getAuthenticationManager().authenticate(bearerAuthentication);

  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    SecurityContextHolder.getContext().setAuthentication(authResult);
  }
}