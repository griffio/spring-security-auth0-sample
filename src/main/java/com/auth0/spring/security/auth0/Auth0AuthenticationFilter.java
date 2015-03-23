package com.auth0.spring.security.auth0;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class Auth0AuthenticationFilter extends GenericFilterBean {

  private static final Logger logger = LoggerFactory.getLogger(Auth0AuthenticationFilter.class);

  private final AuthenticationFailureHandler failureHandler;

  private AuthenticationManager authenticationManager;

  public Auth0AuthenticationFilter() {
    this.failureHandler = new SimpleUrlAuthenticationFailureHandler();
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String authorizationHeader = httpRequest.getHeader("Authorization");

    try {
      if (StringUtils.isEmpty(authorizationHeader)) {
        throw new Auth0TokenException("Authorization header missing from request. Expected Authorization:");
      }
      try {
        logger.info(authorizationHeader);
        Auth0BearerAuthentication authentication = Auth0BearerAuthentication.create(authorizationHeader);
        Authentication authenticated = authenticationManager().authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticated);
        chain.doFilter(request, response);
      } catch (ParseException e) {
        throw new Auth0TokenException(e);
      }
    } catch (AuthenticationException e) {
      logger.error("AuthenticationException", e);
      failureHandler.onAuthenticationFailure(httpRequest, httpResponse, e);
    }

  }

  public AuthenticationManager authenticationManager() {
    return authenticationManager;
  }

  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }
}