package net.buchlese.bofc.jdbi.bofc;

import java.util.List;

import net.buchlese.bofc.api.shift.Employee;
import net.buchlese.bofc.api.shift.Shift;
import net.buchlese.bofc.api.shift.Shop;
import net.buchlese.bofc.api.shift.ShopEvent;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

public interface ShiftCalDAO {

	@RegisterMapper(ShiftMapper.class)
	@SqlQuery("select * from shift where date_ between :datefrom and :datetill")
	List<Shift> fetchShifts(@Bind("datefrom") DateTime date, @Bind("datetill") DateTime datetill);

	@RegisterMapper(ShopEventMapper.class)
	@SqlQuery("select * from shopevent where date_ >= :date and date_ <= :datetill")
	List<ShopEvent> fetchShopEvent(@Bind("date") DateTime date, @Bind("datetill") DateTime datetill);

	@RegisterMapper(EmployeeMapper.class)
	@SqlQuery("select * from employee")
	List<Employee> fetchEmployees();

	@RegisterMapper(ShopMapper.class)
	@SqlQuery("select * from shop ")
	List<Shop> fetchShops();

}
