package net.buchlese.posa.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.api.bofc.ServerState;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;

@Singleton
public class ServerStateGatherer extends AbstractGatherer {

	@Inject private PosCashBalanceDAO cashbalDao;
	private ServerState lastServerState = new ServerState();

	private int count = 0;
	
	public void delayedGatherData() {
		if (count > 16) {
			count = 0;
			gatherData();
		}
		count++;
	}

	@Override
	public void gatherData() {
		lastServerState = new ServerState();
		try {
			lastServerState.setIpAddress(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		lastServerState.setLastDbConnection(new LocalDateTime(SyncTimer.lastRunWithDbConnection));
		lastServerState.setLastSyncRun(new LocalDateTime(SyncTimer.lastRunWithDbConnection));
		lastServerState.setLastHomingRun(new LocalDateTime(SyncTimer.lastRunWithDbConnection));
		lastServerState.setSyncDuration(SyncTimer.lastRunWithDbConnection);
		lastServerState.setTimest(LocalDateTime.now());
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
		PosAdapterApplication.homingQueue.offer(lastServerState); // sync back home
	}

	public ServerState getState() {
		return lastServerState;
	}

}
