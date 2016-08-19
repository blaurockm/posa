package net.buchlese.verw;

import java.time.LocalDate;
import java.time.LocalDateTime;

import net.buchlese.verw.util.LocalDateDeserializer;
import net.buchlese.verw.util.LocalDateSerializer;
import net.buchlese.verw.util.LocalDateTimeDeserializer;
import net.buchlese.verw.util.LocalDateTimeSerializer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootApplication
@EntityScan(basePackages= {"net.buchlese.bofc.api.coupon","net.buchlese.bofc.api.subscr","net.buchlese.bofc.api.bofc"})
public class VerwaltungsApp {
	

	@Bean
	@Primary
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
	
	public static void main(String[] args) {
		SpringApplication.run(VerwaltungsApp.class, args);
	}
}
