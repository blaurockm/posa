package net.buchlese.bofc.api.shift;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Employee {
	private static Map<Long, Employee> employeeMapping;

	@JsonIgnore
	public static  Map<Long, Employee> getEmployees() {
		return employeeMapping;
	}

	@JsonIgnore
	public static  Employee getEmployee(long num) {
		return employeeMapping.get(num);
	}

	private long id;
	private int personellNumber;
	private String name;
	private LocalDate entry;
	private List<Schedule> schedule;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getPersonellNumber() {
		return personellNumber;
	}
	public void setPersonellNumber(int personellNumber) {
		this.personellNumber = personellNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getEntry() {
		return entry;
	}
	public void setEntry(LocalDate entry) {
		this.entry = entry;
	}
	public List<Schedule> getSchedule() {
		return schedule;
	}
	public void setSchedule(List<Schedule> schedule) {
		this.schedule = schedule;
	}

	public static void inject(List<Employee> emps) {
		employeeMapping = new HashMap<Long, Employee>();
		emps.stream().forEach(x -> employeeMapping.put(x.getId(), x));
	}
	
}
