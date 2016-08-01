package net.buchlese.verw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages= {"net.buchlese.bofc.api.coupon"})
public class VerwaltungsApp {

	public static void main(String[] args) {
		SpringApplication.run(VerwaltungsApp.class, args);
	}
}
