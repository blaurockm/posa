package net.buchlese.bofc.core.shift;

import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;

import com.vaadin.ui.components.calendar.event.BasicEvent;

import net.buchlese.bofc.api.shift.Employee;
import net.buchlese.bofc.api.shift.ShopHours;
import net.buchlese.bofc.api.shift.Store;

public class ShiftModel {
	
	public void createDefaultShiftsPerMonth(Employee emp, YearMonth month) {
		
	}

	public List<BasicEvent> getEventsPerStore(Store store, LocalDate from, LocalDate till) {
		return Collections.emptyList();
	}

	public List<BasicEvent> getEventsPerEmployee(Store store, LocalDate from, LocalDate till) {
		return Collections.emptyList();
	}

	public List<BasicEvent> getEventsPerStore(Store store, YearMonth month) {
		return Collections.emptyList();
	}
	
	public List<BasicEvent> getEventsPerEmployee(Employee store, YearMonth month) {
		return Collections.emptyList();
	}
	
	public List<Employee> getEmployees() {
		return Collections.emptyList();
	}
	
	public List<Store> getStores() {
		return Collections.emptyList();
	}
	
	public List<ShopHours> getShopHours(Store store) {
		return Collections.emptyList();
	}
	
	public void addShift(Employee emp, Store store, LocalDate date, ShopHours opening) {
		addShift(emp, store, date, Duration.between(opening.getOpenFrom(), opening.getOpenTill()));
	}
	
	public void addShift(Employee emp, Store store, LocalDate date, Duration workTime) {
		
	}

	public void deleteShift(long id) {
		
	}
	
}
