package io.hhplus.concert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class ConcertReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcertReservationApplication.class, args);
	}

}
