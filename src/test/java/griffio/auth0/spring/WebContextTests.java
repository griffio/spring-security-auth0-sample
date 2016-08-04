package griffio.auth0.spring;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.Date;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class WebContextTests {

  @Resource
  private WebApplicationContext context;

  @Resource
  private FilterChainProxy defaultSecurityFilterChain;

  private MockMvc mvc;

  @Value(value = "${auth0.clientId}")
  private String clientId;

  @Value(value = "${auth0.clientSecret}")
  private String clientSecret;

  @Value(value = "${auth0.domain}")
  private String issuer;

  @Before
  public void setup() {

    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .addFilters(defaultSecurityFilterChain)
        .build();
  }

  @Test
  public void handshake_is_allowed() throws Exception {
    mvc.perform(get("/handshake")).andExpect(status().isOk()).andExpect(content().string("OK"));
  }

  @Test
  public void handshake_is_not_allowed() throws Exception {
    mvc.perform(get("/authorised/handshake")).andExpect(status().isUnauthorized());
  }

  @Test
  public void index_html_is_allowed() throws Exception {
    mvc.perform(get("/index.html")).andExpect(status().isOk());
  }

  @Test
  public void favicon_is_allowed() throws Exception {
    mvc.perform(get("/favicon.ico")).andExpect(status().isOk());
  }

  @Test
  public void authorised_handshake_is_allowed() throws Exception {

    Date now = new Date();

    Claims claims = Jwts.claims()
        .setAudience(clientId)
        .setIssuer("griff.io")
        .setIssuedAt(now)
        .setNotBefore(now)
        .setSubject("test.work");

    byte[] signingKey = Base64.getUrlDecoder().decode(clientSecret);

    String jwt = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, signingKey).compact();

    String bearerToken = String.format("Bearer %s", jwt);

    mvc.perform(get("/authorised/handshake")
        .header(AUTHORIZATION, bearerToken).header(ORIGIN, "example.com"))
        .andExpect(status().isOk())
        .andExpect(content().string("OK"));
  }

}