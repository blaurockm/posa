package net.buchlese.verw.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import net.buchlese.bofc.api.sys.SequenceGen;
import net.buchlese.verw.repos.InvoiceRepository;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("integrationtest")
@Ignore
public class SequenceGeneratorTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InvoiceRepository repository;

    @Autowired 
    private SequenceGenerator generator;
    
    @Test
    public void testSequence() throws Exception {
    	generator.initSequence("testKey", 666, LocalDate.now());
    	
    	SequenceGen x = entityManager.find(SequenceGen.class, 1L);
    	
    	assertThat(x.getSeqKey()).isEqualTo("testKey");
    }
    
}
