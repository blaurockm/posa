package net.buchlese.verw.ctrl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.querydsl.core.types.Predicate;

import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.verw.repos.CustomerRepository;
import net.buchlese.verw.repos.SubscrArticleRepository;
import net.buchlese.verw.repos.SubscrDeliveryRepository;
import net.buchlese.verw.repos.SubscrIntervalDeliveryRepository;
import net.buchlese.verw.repos.SubscrIntervalRepository;
import net.buchlese.verw.repos.SubscrProductRepository;
import net.buchlese.verw.repos.SubscriptionRepository;
import net.buchlese.verw.util.LocalDateDeserializer;
import net.buchlese.verw.util.LocalDateSerializer;
import net.buchlese.verw.util.LocalDateTimeDeserializer;
import net.buchlese.verw.util.LocalDateTimeSerializer;

@RunWith(SpringRunner.class)
@WebMvcTest(SubscrProductController.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableSpringDataWebSupport
@ActiveProfiles("integrationtest")
public class SubscrProductControllerTest {

	private JacksonTester<SubscrInterval> intvlJson;

	private JacksonTester<SubscrIntervalDelivery> intvlDelivJson;

	private JacksonTester<SubscrArticle> articleJson;

	private JacksonTester<SubscrDelivery> artDelivJson;

	@Before
	public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        objectMapper.registerModule(javaTimeModule);
		// Possibly configure the mapper
		JacksonTester.initFields(this, objectMapper);
	}
	@Autowired
	private MockMvc mvc;

	@MockBean
	private	SubscrProductRepository subscrProductRepo;

	@MockBean
	private	SubscrArticleRepository articleRepo;

	@MockBean
	private	SubscrDeliveryRepository deliveryRepo;

	@MockBean
	private	SubscrIntervalRepository intervalRepo;
	@MockBean
	private	SubscrIntervalDeliveryRepository intervalDeliveryRepo;

	@MockBean
	private	CustomerRepository customerRepo;
	@MockBean
	private	SubscriptionRepository subscriptionRepo;


	// subscrproductsDyn
	@Test
	public void testFindByExample() throws Exception {
		SubscrProduct sp = new SubscrProduct();
		sp.setAbbrev("abbr");
		sp.setHalfPercentage(0.5d);
		sp.setEndDate(new java.sql.Date(System.currentTimeMillis()));
		given(this.subscrProductRepo.findAll(org.mockito.Matchers.any(Predicate.class), org.mockito.Matchers.any(Pageable.class)))
		.willReturn(new PageImpl<>(Arrays.asList(sp)));

		this.mvc.perform(get("/subscrproducts/subscrproductsDyn").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(jsonPath("$.content[0].halfPercentage").value(0.5d));
	}

	// createinterval
	@Test
	public void testCreateInterval() throws Exception {
		SubscrProduct sp = new SubscrProduct();
		sp.setId(312L);
		sp.setPayPerDelivery(false); // important
		sp.setLastInterval(new java.sql.Date(System.currentTimeMillis()));
		sp.setIntervalType(PayIntervalType.MONTHLY);
		sp.setIntervalPattern("hallo #");

		SubscrInterval si = new SubscrInterval();
		si.setId(444L);
		si.setBrutto(500);
		si.setProductId(312);

		when(this.subscrProductRepo.findOne(org.mockito.Matchers.eq(312L))).thenReturn(sp);


		MvcResult res = this.mvc.perform(post("/subscrproducts/createinterval").contentType(MediaType.APPLICATION_JSON)
				.content(intvlJson.write(si).getJson()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andReturn();

		
		verify(this.subscrProductRepo).findOne(org.mockito.Matchers.eq(312L));
		verify(this.subscrProductRepo).save(org.mockito.Matchers.eq(sp));
		verify(this.intervalRepo).saveAndFlush(org.mockito.Matchers.eq(si));

		SubscrInterval siRet = intvlJson.parse(res.getResponse().getContentAsString()).getObject();

//		System.out.println(res.getResponse().getContentAsString());
		assertThat(siRet.getProductId()).isEqualTo(312L);
		assertThat(siRet.getName()).isNotEmpty();
		assertThat(siRet.getStartDate().toLocalDate()).isEqualTo(LocalDate.now().plusDays(1));
	}

	/**
	 * wir erwwarten dass es scheppert. Es darf kein Interval angelegt werden für eine Fortsetzung.
	 * @throws Exception
	 */
	@Test
	public void testCreateIntervalError() throws Exception {
		SubscrProduct sp = new SubscrProduct();
		sp.setId(312L);
		sp.setPayPerDelivery(true);
		sp.setLastInterval(new java.sql.Date(System.currentTimeMillis()));
		sp.setIntervalType(PayIntervalType.MONTHLY);
		sp.setIntervalPattern("hallo #");

		SubscrInterval si = new SubscrInterval();
		si.setId(444L);
		si.setBrutto(500);
		si.setProductId(312);

		when(this.subscrProductRepo.findOne(org.mockito.Matchers.eq(312L))).thenReturn(sp);

		this.mvc.perform(post("/subscrproducts/createinterval").contentType(MediaType.APPLICATION_JSON)
				.content(intvlJson.write(si).getJson()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(422));
	}

	// createintervaldelivery
	@Test
	public void testCreateIntervalDelivery() throws Exception {
		SubscrProduct sp = new SubscrProduct();
		sp.setId(312L);
		sp.setPayPerDelivery(false);
		
		SubscrInterval si = new SubscrInterval();
		si.setId(444L);
		si.setBrutto(500);
		si.setProductId(312);

		Subscriber sub = new Subscriber();
		sub.setId(555L);

		Subscription s = new Subscription();
		s.setId(666L);
		s.setProduct(sp);
		
		SubscrIntervalDelivery sid = new SubscrIntervalDelivery();
		sid.setIntervalId(444L);
		sid.setSubscriberId(555L);
		sid.setSubscriptionId(666L);
	
		when(this.subscrProductRepo.findOne(org.mockito.Matchers.eq(312L))).thenReturn(sp);
		when(this.intervalRepo.findOne(org.mockito.Matchers.eq(444L))).thenReturn(si);
		when(this.customerRepo.findOne(org.mockito.Matchers.eq(555L))).thenReturn(sub);
		when(this.subscriptionRepo.findOne(org.mockito.Matchers.eq(666L))).thenReturn(s);

		
		MvcResult res = this.mvc.perform(post("/subscrproducts/createintervaldelivery").contentType(MediaType.APPLICATION_JSON)
				.content(intvlDelivJson.write(sid).getJson()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andReturn();
		SubscrIntervalDelivery sidRet = intvlDelivJson.parse(res.getResponse().getContentAsString()).getObject();

//		System.out.println(res.getResponse().getContentAsString());
		assertThat(sidRet.isPayed()).isFalse();
		assertThat(sidRet.getDeliveryDate().toLocalDate()).isEqualTo(LocalDate.now());
	}


	// createarticle
	@Test
	public void testCreateArticle() throws Exception {
		SubscrProduct sp = new SubscrProduct();
		sp.setId(312L);
		sp.setPayPerDelivery(true); // important
		sp.setCount(32);
		sp.setNamePattern("hallo #");

		SubscrArticle si = new SubscrArticle();
		si.setId(444L);
		si.setBrutto(500);
		si.setProductId(312);

		when(this.subscrProductRepo.findOne(org.mockito.Matchers.eq(312L))).thenReturn(sp);


		MvcResult res = this.mvc.perform(post("/subscrproducts/createarticle").contentType(MediaType.APPLICATION_JSON)
				.content(articleJson.write(si).getJson()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andReturn();

		
		verify(this.subscrProductRepo).findOne(org.mockito.Matchers.eq(312L));
		verify(this.subscrProductRepo).save(org.mockito.Matchers.eq(sp));
		verify(this.articleRepo).saveAndFlush(org.mockito.Matchers.eq(si));

		SubscrArticle siRet = articleJson.parse(res.getResponse().getContentAsString()).getObject();

//		System.out.println(res.getResponse().getContentAsString());
		assertThat(siRet.getProductId()).isEqualTo(312L);
		assertThat(siRet.getName()).isNotEmpty();
		assertThat(siRet.getIssueNo()).isEqualTo(33);
	}
	
	// createarticledelivery
	@Test
	public void testCreateDelivery() throws Exception {
		SubscrProduct sp = new SubscrProduct();
		sp.setId(312L);
		sp.setPayPerDelivery(false);
		
		SubscrArticle si = new SubscrArticle();
		si.setId(444L);
		si.setBrutto(500);
		si.setProductId(312);

		Subscriber sub = new Subscriber();
		sub.setId(555L);
		sub.setNeedsDeliveryNote(false);

		Subscription s = new Subscription();
		s.setId(666L);
		s.setProduct(sp);
		
		SubscrDelivery sid = new SubscrDelivery();
		sid.setArticleId(444L);
		sid.setSubscriberId(555L);
		sid.setSubscriptionId(666L);
	
		when(this.subscrProductRepo.findOne(org.mockito.Matchers.eq(312L))).thenReturn(sp);
		when(this.articleRepo.findOne(org.mockito.Matchers.eq(444L))).thenReturn(si);
		when(this.customerRepo.findOne(org.mockito.Matchers.eq(555L))).thenReturn(sub);
		when(this.subscriptionRepo.findOne(org.mockito.Matchers.eq(666L))).thenReturn(s);

		
		MvcResult res = this.mvc.perform(post("/subscrproducts/createarticledelivery").contentType(MediaType.APPLICATION_JSON)
				.content(artDelivJson.write(sid).getJson()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andReturn();
		SubscrDelivery sidRet = artDelivJson.parse(res.getResponse().getContentAsString()).getObject();

//		System.out.println(res.getResponse().getContentAsString());
		assertThat(sidRet.isPayed()).isFalse();
		assertThat(sidRet.isSlipped()).isTrue();
		assertThat(sidRet.getDeliveryDate().toLocalDate()).isEqualTo(LocalDate.now());
	}



}
