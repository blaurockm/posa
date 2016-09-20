package net.buchlese.verw;

import java.time.LocalDate;
import java.time.LocalDateTime;

import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.verw.util.LocalDateDeserializer;
import net.buchlese.verw.util.LocalDateSerializer;
import net.buchlese.verw.util.LocalDateTimeDeserializer;
import net.buchlese.verw.util.LocalDateTimeSerializer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootApplication
@EntityScan(basePackages= {"net.buchlese.bofc.api.coupon","net.buchlese.bofc.api.subscr","net.buchlese.bofc.api.bofc","net.buchlese.bofc.api.sys"})
public class VerwaltungsApp extends RepositoryRestConfigurerAdapter {
	
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(Subscriber.class);
		config.exposeIdsFor(SubscrProduct.class);
		config.exposeIdsFor(SubscrInterval.class);
		config.exposeIdsFor(SubscrArticle.class);
		config.exposeIdsFor(Subscription.class);
	}

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

//	@Bean
//	public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() {
//
//		Resource sourceData = new ClassPathResource("defaultArticleGroups.json");
//
//		Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
//		factory.setResources(new Resource[] { sourceData });
//		return factory;
//	}
	  
	public static void main(String[] args) {
		SpringApplication.run(VerwaltungsApp.class, args);
	}
}
