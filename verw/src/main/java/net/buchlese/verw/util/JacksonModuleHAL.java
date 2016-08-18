package net.buchlese.verw.util;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

@Component
public class JacksonModuleHAL extends SimpleModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	  public void setupModule(SetupContext context) {

	    SimpleSerializers serializers = new SimpleSerializers();
	    SimpleDeserializers deserializers = new SimpleDeserializers();

	    serializers.addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
	    deserializers.addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);

	    context.addSerializers(serializers);
	    context.addDeserializers(deserializers);
	  }
}
