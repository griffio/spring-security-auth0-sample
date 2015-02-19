package griffio.auth0.spring.mvc;

import com.auth0.spring.security.auth0.Auth0UserDetails;
import com.auth0.spring.security.auth0.CurrentAuth0UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HandshakeController {

  Logger log = LoggerFactory.getLogger(HandshakeController.class);

  @RequestMapping(value = "/handshake")
  @ResponseBody
  public String handshake() {
    return "OK";
  }

  @RequestMapping(value = "/authorised/handshake")
  @ResponseBody
  public String authorisedHandshake(@CurrentUser CurrentUserDetails auth0User, @CurrentAuth0UserDetails Auth0UserDetails auth0UserDetails) {
    log.info(auth0User.toString());
    log.info(auth0UserDetails.getUsername());
    return "OK";
  }

}
