package griffio.auth0.spring.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HandshakeController {

  private static Logger log = LoggerFactory.getLogger(HandshakeController.class);

  @RequestMapping(value = "/handshake")
  @ResponseBody
  public String handshake() {
    return "OK";
  }

  @RequestMapping(value = "/authorised/handshake")
  @CrossOrigin
  @ResponseBody
  public String authorisedHandshake(@SubjectToken String tokenSubject) {
    log.info(tokenSubject);
    return "OK";
  }

}