package griffio.auth0.spring.mvc;

import griffio.auth0.spring.security.Auth0User;
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
	public String authorisedHandshake(@CurrentUser Auth0User auth0User) {
		log.info(auth0User.toString());
		return "OK";
	}

}
