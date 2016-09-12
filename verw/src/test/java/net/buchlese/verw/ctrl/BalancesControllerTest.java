package net.buchlese.verw.ctrl;

//import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.querydsl.core.types.Predicate;

import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.verw.core.AccountingExportFile;
import net.buchlese.verw.core.BalanceAccounting;
import net.buchlese.verw.core.InvoiceAccounting;
import net.buchlese.verw.reports.ReportBalanceExportCreator;
import net.buchlese.verw.repos.ArticleGroupRepository;
import net.buchlese.verw.repos.BalanceExportRepository;
import net.buchlese.verw.repos.BalanceRepository;


@RunWith(SpringRunner.class)
@WebMvcTest(BalancesController.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableSpringDataWebSupport
public class BalancesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private	BalanceRepository balanceRepo;

    @MockBean
    private BalanceExportRepository exportRepository;

    @MockBean
    private AccountingExportFile exportFileCreator;


	@MockBean 
	private BalanceAccounting balanceAccounting;

	@MockBean 
	private InvoiceAccounting invoiceAccounting;

	@MockBean 
	private ArticleGroupRepository articleGroup;

	@MockBean
    private ReportBalanceExportCreator reportBalanceExport;
    
    @Test
    public void testExample() throws Exception {
    	PosCashBalance pcb = new PosCashBalance();
    	pcb.setAbschlussId("20140601");
    	pcb.setId(2L);
    	pcb.setCash(200L);
    	
    	
        given(this.balanceRepo.findAll(org.mockito.Matchers.any(Predicate.class), org.mockito.Matchers.any(Pageable.class)))
                .willReturn(new PageImpl<>(Arrays.asList(pcb)));
        
        this.mvc.perform(get("/balances/balancesDyn").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.content[0].cash").value(200));
        
//        this.mvc.perform(get("/balances/balancesDyn").accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.content[0].cash").value(200));
        
        
    }
	
}
