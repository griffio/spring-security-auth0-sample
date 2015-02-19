package griffio.auth0.spring;

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
import java.util.Collections;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        mvc.perform(get("/handshake")).andExpect(status().isOk());
    }

    @Test
    public void handshake_is_not_allowed() throws Exception {
        mvc.perform(get("/authorised/handshake")).andExpect(status().isForbidden());
    }

    @Test
    public void authorised_handshake_is_allowed() throws Exception {
        Map<String, Object> params = Collections.<String, Object>singletonMap("username", "bobert");
        String encoded = new JWTEncoding(clientId, clientSecret).encode(params);
        String bearerToken = String.format("Bearer %s", encoded);
        mvc.perform(get("/authorised/handshake").header("Authorization", bearerToken)).andExpect(status().isOk());
    }

}