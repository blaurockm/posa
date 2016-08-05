package net.buchlese.verw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@SpringBootApplication
@EntityScan(basePackages= {"net.buchlese.bofc.api.coupon","net.buchlese.bofc.api.subscr"})
public class VerwaltungsApp {
	
	@Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

	public static void main(String[] args) {
		SpringApplication.run(VerwaltungsApp.class, args);
	}
}
