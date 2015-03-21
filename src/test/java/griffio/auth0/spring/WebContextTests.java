package griffio.auth0.spring;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
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
    JWTClaimsSet claimsSet = new JWTClaimsSet();
    claimsSet.setIssuer("example.com");
    claimsSet.setSubject("test.user");
    claimsSet.setAudience("localhost");
    SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
    signedJWT.sign(new MACSigner(Base64.getUrlDecoder().decode(clientSecret)));
    String bearerToken = String.format("Bearer %s", signedJWT.serialize());
    mvc.perform(get("/authorised/handshake")
        .header("Authorization", bearerToken))
        .andExpect(status().isOk())
        .andExpect(content().string("OK"));
  }

}