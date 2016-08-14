package net.buchlese.verw.util;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JPAMapper {

	@Autowired
	private ObjectMapper mapper;
	
	@PostConstruct
	public void init() {
		MAPPER = mapper;
	}
	
	public static ObjectMapper MAPPER;

	public static String writeValueAsString(Object value) {
		try {
			return MAPPER.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			return e.getMessage();
		}
	}

	public static <T> T readValue(String bals, Class<T> valueType) {
		try {
			return MAPPER.readValue(bals, valueType);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
