package griffio.auth0.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({ "classpath:auth0.properties" })
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
