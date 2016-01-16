package griffio.auth0.spring.security;

import com.auth0.spring.security.auth0.Auth0AuthenticationEntryPoint;
import com.auth0.spring.security.auth0.Auth0AuthenticationFilter;
import com.auth0.spring.security.auth0.Auth0AuthenticationProvider;
import com.auth0.spring.security.auth0.Auth0MACProvider;
import com.nimbusds.jose.crypto.MACVerifier;
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
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import java.time.Clock;
import java.util.Base64;

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

  @Bean
  public Auth0MACProvider macProvider() throws Exception {
    return new Auth0MACProvider(new MACVerifier(Base64.getUrlDecoder().decode(clientSecret.getBytes())));
  }

  @Bean
  public AuthenticationProvider authenticationProvider() throws Exception {
    log.info("{}:{}", clientId, clientSecret);
    Auth0AuthenticationProvider authenticationProvider;
    authenticationProvider = new Auth0AuthenticationProvider(clientId, macProvider(), issuer, systemClock());
    return authenticationProvider;
  }

  @Bean
  public Clock systemClock() {
    return Clock.systemUTC();
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
        .and().antMatcher("/authorised/**").authorizeRequests().anyRequest().hasRole("USER")
        .and().addFilterAfter(authenticationFilter, SecurityContextPersistenceFilter.class)
        .addFilterBefore(new SimpleCORSFilter(), ChannelProcessingFilter.class)
        .exceptionHandling().authenticationEntryPoint(auth0AuthenticationEntryPoint);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider());
  }

}
