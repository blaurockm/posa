package net.buchlese.verw.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import net.buchlese.bofc.api.bofc.PaymentMethod;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.api.bofc.PosTicket;
import net.buchlese.bofc.api.bofc.PosTx;
import net.buchlese.bofc.api.bofc.Tax;
import net.buchlese.bofc.api.bofc.TxType;

@RunWith(SpringRunner.class)
@JsonTest
@ActiveProfiles("integrationtest")
public class PosCashBalanceJsonTest {
	
    @Autowired
    private JacksonTester<PosCashBalance> json;

    @Test
    public void testSerialize() throws Exception {
    	PosCashBalance bal = new PosCashBalance();
    	bal.setId(750L);
    	bal.setAbschlussId("20160907");
    	Map<Tax,Long> taxMap = new HashMap<>();
    	taxMap.put(Tax.FULL, 9287L);
    	taxMap.put(Tax.HALF, 14996L);
    	bal.setTaxBalance(taxMap);
    	bal.setCashIn(Collections.emptyMap());
    	bal.setFirstTimestamp(LocalDateTime.ofInstant(new java.util.Date(1473231254000L).toInstant(), ZoneId.systemDefault()));
    	bal.setOrigAbschluss("{\"id\":3214,\"kasse\":1,\"abschlussnr\":2386,\"abschlussid\":\"20160907\",\"bar\":658.99,\"telecashIst\":53.1,\"anfang\":416.03,\"ist\":712.09,\"soll\":711.96,\"differenz\":0.13,\"abschoepfung\":240.0,\"einzahlungen\":0.0,\"auszahlungen\":0.0,\"gutscheine\":10.0,\"gutschriften\":-10.0,\"umsatzVoll\":92.87,\"umsatzHalb\":149.96,\"umsatzOhne\":0.0,\"bezahlteRechnungen\":53.1,\"anzahlKunden\":19,\"ladenUmsatz\":62.8,\"besorgungsUmsatz\":180.03,\"vonDatum\":1473199201000,\"bisDatum\":1473285540000,\"auszahlungenVoll\":0.0,\"auszahlungenHalb\":0.0,\"auszahlungenOhne\":0.0,\"zeitmarke\":12857903}");
    	bal.setExported(true);
    	List<PosTicket> tickets = new ArrayList<>();
    	PosTicket t = new PosTicket();
    	t.setBelegNr(11509937);
    	t.setPointid(0);
    	t.setPaymentMethod(PaymentMethod.CASH);
    	t.setTotal(2958L);
    	t.setTimestamp(LocalDateTime.ofInstant(new java.util.Date(1473231246000L).toInstant(), ZoneId.systemDefault()));
    	List<PosTx> txs = new ArrayList<>();
    	PosTx tx = new PosTx();
    	tx.setArticleId(101116);
    	tx.setBelegIdx(1);
    	tx.setBelegNr(11509937);
    	tx.setArticleKey("A00025YD");
    	tx.setArticleGroupKey("book");
    	tx.setIsbn("9783894254452");
    	tx.setRebate(.0);
    	tx.setTotal(799L);
    	tx.setEan(null);
    	tx.setTax(Tax.HALF);
    	tx.setType(TxType.SELL);
    	tx.setTimestamp(LocalDateTime.ofInstant(new java.util.Date(1473231254000L).toInstant(), ZoneId.systemDefault()));
    	txs.add(tx);
    	t.setTxs(txs);
    	tickets.add(t);
    	bal.setTickets(tickets);
        // Assert against a `.json` file in the same package as the test
        assertThat(this.json.write(bal)).isEqualToJson("poscashbalance.json");
    }

    
    @Test
    public void testDeserialize() throws Exception {
    	String content = FileCopyUtils.copyToString(new InputStreamReader(new ClassPathResource("net/buchlese/verw/api/sendbofc.json").getInputStream()));
        assertThat(this.json.parseObject(content).getTicketCount()).isEqualTo(19);
    }
    
    
}
