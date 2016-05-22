package net.buchlese.bofc.jdbi.bofc;

import java.util.List;

import javax.validation.Valid;

import net.buchlese.bofc.api.bofc.UserChange;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(UserChangeMapper.class)
public interface UserChangeDAO {

	@SqlQuery("select * from userchange where objectid = :obj ")
	List<UserChange> fetch(@Bind("objid") long obj);
	
	@SqlUpdate("insert into userchange (objectid, login, fieldId, oldValue, newValue, action, modDate) "
			+ " values (:objectId, :login, :fieldId, :oldValue, :newValue, :action, :modDate) ")
	void insert(@Valid @BindBean UserChange u);

}
