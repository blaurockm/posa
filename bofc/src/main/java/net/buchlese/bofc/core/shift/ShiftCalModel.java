package net.buchlese.bofc.core.shift;

import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.buchlese.bofc.api.shift.Employee;
import net.buchlese.bofc.api.shift.Shift;
import net.buchlese.bofc.api.shift.Shop;
import net.buchlese.bofc.api.shift.ShopEvent;
import net.buchlese.bofc.api.shift.ShopHours;
import net.buchlese.bofc.jdbi.bofc.ShiftCalDAO;

import com.google.inject.Inject;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.components.calendar.event.BasicEvent;

public class ShiftCalModel {
	
	@Inject private ShiftCalDAO dao;
	
	public void createDefaultShiftsPerMonth(Employee emp, YearMonth month) {
		
	}

	public List<BasicEvent> getEventsPerStore(Shop store, LocalDate from, LocalDate till) {
		return Collections.emptyList();
	}

	public List<BasicEvent> getEventsPerEmployee(Shop store, LocalDate from, LocalDate till) {
		return Collections.emptyList();
	}

	public List<BasicEvent> getEventsPerStore(Shop store, YearMonth month) {
		LocalDate first = month.atDay(1);
		org.joda.time.DateTime from = new org.joda.time.DateTime(first.getYear(), first.getMonthValue(), 1, 0 ,0);
		LocalDate last = month.atEndOfMonth();
		org.joda.time.DateTime till = new org.joda.time.DateTime(last.getYear(), last.getMonthValue(), last.getDayOfMonth(), 23, 59);
		List<BasicEvent> res = new ArrayList<BasicEvent>();
		
		List<Shift> shifts = dao.fetchShifts(from, till);
		
		shifts.stream().forEach(x -> res.add(convertShiftToEvent(x)));
		
		List<ShopEvent> events = dao.fetchShopEvent(from, till);

		events.stream().forEach(x -> res.add(convertShopEventsToEvent(x)));

		return res;
	}
	
	private BasicEvent convertShopEventsToEvent(ShopEvent x) {
		BasicEvent ev = new BasicEvent();
		ev.setCaption("ShopEevnt");
		ev.setDescription("Beschreibung");
//		ev.setStyleName("style");
		ev.setStart(java.util.Date.from(x.getFrom()));
		ev.setEnd(java.util.Date.from(x.getTill()));
		ev.setAllDay(x.isWholeDay());
		return ev;
	}

	private BasicEvent convertShiftToEvent(Shift x) {
		BasicEvent ev = new BasicEvent();
		ev.setCaption("Schicht");
		ev.setDescription("Beschreibung");
//		ev.setStyleName("style");
		ev.setStart(java.util.Date.from(x.getWorkFrom()));
		ev.setEnd(java.util.Date.from(x.getWorkTill()));
		ev.setAllDay(false);
		return ev;
	}

	public List<BasicEvent> getEventsPerEmployee(Employee store, YearMonth month) {
		return Collections.emptyList();
	}
	
	public List<Employee> getEmployees() {
		return Collections.emptyList();
	}
	
	public List<Shop> getStores() {
		return Collections.emptyList();
	}
	
	public List<ShopHours> getShopHours(Shop store) {
		return Collections.emptyList();
	}
	
	public void addShift(Employee emp, Shop store, LocalDate date, ShopHours opening) {
		addShift(emp, store, date, Duration.between(opening.getOpenFrom(), opening.getOpenTill()));
	}
	
	public void addShift(Employee emp, Shop store, LocalDate date, Duration workTime) {
		
	}

	public void deleteShift(long id) {
		
	}

	private List<BasicEvent> events;
	
	public void defaultSelect() {
		// alle Shifts für diesen Monat
		events = getEventsPerStore(null, YearMonth.now());
	}
	
	public Indexed getContainer() {
		if (events != null) {
			return createContainer(events);
		}
		return createContainer(Collections.emptyList());
	}
	
	private Container.Indexed createContainer(List<BasicEvent> ev) {
		final BeanItemContainer<BasicEvent> co =
			    new BeanItemContainer<BasicEvent>(BasicEvent.class);        
		
		ev.forEach(x -> co.addBean(x));
		// The container must be ordered by the start time. You
		// have to sort the BIC every time after you have added
		// or modified events.
		co.sort(new Object[]{"start"}, new boolean[]{true});

		return co;
	}
}
