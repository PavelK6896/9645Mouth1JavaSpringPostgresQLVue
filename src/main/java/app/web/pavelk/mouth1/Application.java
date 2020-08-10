package app.web.pavelk.mouth1;

import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
       // Sentry.capture("Application start"); //log
        SpringApplication.run(Application.class, args);
    }

}
