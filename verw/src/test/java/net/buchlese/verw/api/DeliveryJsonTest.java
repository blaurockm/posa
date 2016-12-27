package net.buchlese.verw.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import net.buchlese.bofc.api.subscr.SubscrDelivery;

@RunWith(SpringRunner.class)
@JsonTest
@ActiveProfiles("integrationtest")
public class DeliveryJsonTest {
	
    @Autowired
    private JacksonTester<SubscrDelivery> json;

    @Test
    public void testSerialize() throws Exception {
    	SubscrDelivery deli = new SubscrDelivery();
    	deli.setDeliveryDate(new java.sql.Date(System.currentTimeMillis()));
    	deli.setId(750L);
    	deli.setPayed(false);
    	deli.setQuantity(2);
        // Assert against a `.json` file in the same package as the test
        assertThat(this.json.write(deli)).isEqualToJson("subscrdelivery.json");
    }
    
    
}
