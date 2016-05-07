package net.buchlese.bofc.jdbi.bofc;

import java.util.List;

import javax.validation.Valid;

import net.buchlese.bofc.api.bofc.Mapping;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(MappingMapper.class)
public interface MappingDAO {

	@SqlQuery("select max(debitor) as debitorId, pointid, customer as customerId, max(name1) as name1, "
			+ " count(*) as anz from posinvoice where pointid = :pointid and customer = :customerid group by pointid, customerid order by customerid")
	List<Mapping> fetch(@Bind("pointid") int point, @Bind("customerid") int customerid);

	@SqlQuery("select max(debitor) as debitorId, pointid, customer as customerId, max(name1) as name1, "
			+ " count(*) as anz  from posinvoice where pointid = :pointid group by pointid, customerid order by customerid")
	List<Mapping> fetch(@Bind("pointid") int point);

	@SqlQuery("select max(debitor) as debitorId, pointid, customer as customerId, max(name1) as name1, "
			+ " count(*) as anz  from posinvoice where pointid = :pointid and debitor = 0 group by pointid, customerid order by customerid")
	List<Mapping> fetchEmpty(@Bind("pointid") int point);

	@SqlQuery("select max(debitor) as debitorId, pointid, customer as customerId, max(name1) as name1, "
			+ " count(*) as anz  from posinvoice where debitor = 0 group by pointid, customerid order by pointid, customerid")
	List<Mapping> fetchAllEmpty();

	@SqlUpdate("update posinvoice set debitor = :debitorId where pointid = :pointid and customer = :customerId")
	void update(@Valid @BindBean Mapping inv);

}
