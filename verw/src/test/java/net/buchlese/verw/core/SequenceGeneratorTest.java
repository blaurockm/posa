package net.buchlese.verw.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import net.buchlese.bofc.api.sys.SequenceGen;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("integrationtest")
@ComponentScan(value="net.buchlese.verw.core")
public class SequenceGeneratorTest {
    @Autowired
    private TestEntityManager entityManager;


    @Autowired 
    private SequenceGenerator generator;
    
    @Test
    public void testInitSequence() throws Exception {
    	generator.initSequence("testKey", 666, LocalDate.now());
    	
    	SequenceGen x = entityManager.find(SequenceGen.class, 1L);
    	
    	assertThat(x.getSeqKey()).isEqualTo("testKey");
    }

    @Test
    public void testSequenceNext() throws Exception {
    	generator.initSequence("testKey2", 666, LocalDate.now());
    	
    	Long num1 = generator.getNextNumber("testKey2");
    	
    	assertThat(num1).isEqualTo(667);
    	
    	Long num2 = generator.getNextNumber("testKey2");
    	
    	assertThat(num2).isEqualTo(668);
    	
    	SequenceGen x = entityManager.find(SequenceGen.class, 2L);
    	
    	assertThat(x).isNotNull();
    	assertThat(x.getSeqKey()).isEqualTo("testKey2");
    }
    
    
    
}
