package api.invext.bots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UbotsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UbotsApplication.class, args);
	}

}
