package griffio.auth0.spring.security;

import com.auth0.spring.security.auth0.Auth0AuthenticationEntryPoint;
import com.auth0.spring.security.auth0.Auth0AuthenticationFilter;
import com.auth0.spring.security.auth0.Auth0AuthenticationProvider;
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

@Configuration
@Order(1)
public class SecuredSecurityConfigurer extends WebSecurityConfigurerAdapter {

  @Value(value = "${auth0.clientId}")
  private String clientId;

  @Value(value = "${auth0.clientSecret}")
  private String clientSecret;

  @Bean
  public AuthenticationProvider authenticationProvider() {
    Auth0AuthenticationProvider authenticationProvider;
    authenticationProvider = new Auth0AuthenticationProvider();
    authenticationProvider.setClientId(clientId);
    authenticationProvider.setClientSecret(clientSecret);
    return authenticationProvider;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    Auth0AuthenticationEntryPoint auth0AuthenticationEntryPoint;
    auth0AuthenticationEntryPoint = new Auth0AuthenticationEntryPoint();

    Auth0AuthenticationFilter authenticationFilter;
    authenticationFilter = new Auth0AuthenticationFilter(authenticationManager(), auth0AuthenticationEntryPoint);

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
