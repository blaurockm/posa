package net.buchlese.posa.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.api.bofc.ServerState;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.LocalDate;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ServerStateGatherer extends AbstractGatherer {

	@Inject private PosCashBalanceDAO cashbalDao;
	private ServerState lastServerState = new ServerState();
	
	@Override
	public void gatherData() {
		lastServerState = new ServerState();
		try {
			lastServerState.setIpAddress(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		lastServerState.setLastDbConnection(new Instant(SyncTimer.lastRunWithDbConnection));
		lastServerState.setLastSyncRun(new Instant(SyncTimer.lastRunWithDbConnection));
		lastServerState.setLastHomingRun(new Instant(SyncTimer.lastRunWithDbConnection));
		lastServerState.setSyncDuration(new Duration(SyncTimer.lastRunWithDbConnection));
		lastServerState.setTimest(Instant.now());
		List<PosCashBalance> bals = cashbalDao.fetchAllAfter(LocalDate.now().minusDays(14).toString("yyyyMMdd"));
		LocalDate thisWeekIter = LocalDate.now().withDayOfWeek(1);
		LocalDate lastWeekIter = thisWeekIter.minusWeeks(1);
		boolean[] thisWeek = new boolean[7];
		boolean[] lastWeek = new boolean[7];
		for(int x = 0; x<7; x++) {
			LocalDate compDay = thisWeekIter.plusDays(x);
			LocalDate lastCompDay = lastWeekIter.plusDays(x);
			thisWeek[x] = bals.stream().anyMatch( b -> b.getFirstCovered().toLocalDate().equals(compDay) );
			lastWeek[x] = bals.stream().anyMatch( b -> b.getFirstCovered().toLocalDate().equals(lastCompDay) );
		}
		lastServerState.setThisWeek(thisWeek);
		lastServerState.setLastWeek(lastWeek);
	}

	public ServerState getState() {
		return lastServerState;
	}

}
