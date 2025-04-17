package nvi.server.swcmessenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SwCmessengerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwCmessengerApplication.class, args);
        System.out.println("Hello from World!");
    }

}
