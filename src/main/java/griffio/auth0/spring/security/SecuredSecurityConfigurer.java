package griffio.auth0.spring.security;

import com.auth0.spring.security.auth0.Auth0AuthenticationEntryPoint;
import com.auth0.spring.security.auth0.Auth0AuthenticationFilter;
import com.auth0.spring.security.auth0.Auth0AuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@Order(1)
public class SecuredSecurityConfigurer extends WebSecurityConfigurerAdapter {

  private static Logger log = LoggerFactory.getLogger(SecuredSecurityConfigurer.class);

  @Value(value = "${auth0.clientId}")
  private String clientId;

  @Value(value = "${auth0.clientSecret}")
  private String clientSecret;

  @Value(value = "${auth0.domain}")
  private String issuer;

  @Value(value = "${cors.allowed-origins}")
  private List<String> corsAllowedOrigins;

  @Bean
  public AuthenticationProvider authenticationProvider() throws Exception {
    log.info("{}:{}", clientId, clientSecret);
    Auth0AuthenticationProvider authenticationProvider;
    authenticationProvider = new Auth0AuthenticationProvider(clientId, clientSecret, issuer);
    return authenticationProvider;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    Auth0AuthenticationEntryPoint auth0AuthenticationEntryPoint;
    auth0AuthenticationEntryPoint = new Auth0AuthenticationEntryPoint();

    Auth0AuthenticationFilter authenticationFilter;
    authenticationFilter = new Auth0AuthenticationFilter();
    authenticationFilter.setAuthenticationManager(authenticationManager());
    http.csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().cors() // http://docs.spring.io/spring-security/site/docs/4.1.x/reference/htmlsingle/#cors
        .and().exceptionHandling().authenticationEntryPoint(auth0AuthenticationEntryPoint)
        .and().antMatcher("/authorised/**").authorizeRequests().anyRequest().hasRole("USER")
        .and().addFilterAfter(authenticationFilter, SecurityContextPersistenceFilter.class);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(corsAllowedOrigins);
    configuration.setAllowedMethods(Arrays.asList("GET","POST"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/authorised/**", configuration);
    return source;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider());
  }
}