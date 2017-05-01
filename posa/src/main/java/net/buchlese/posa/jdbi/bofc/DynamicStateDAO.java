package net.buchlese.posa.jdbi.bofc;

import java.util.List;

import net.buchlese.posa.api.bofc.PosSyncState;

import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

public interface DynamicStateDAO {

	@SqlQuery("select * from dynamicState where bigValue is not null")
	@RegisterMapper(PosSyncStateMapper.class)
	List<PosSyncState> getSyncStates();

}
