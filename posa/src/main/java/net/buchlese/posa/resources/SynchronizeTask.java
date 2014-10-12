package net.buchlese.posa.resources;

import io.dropwizard.servlets.tasks.Task;

import java.io.PrintWriter;

import net.buchlese.posa.core.SyncTimer;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ImmutableMultimap;

public class SynchronizeTask extends Task {

	private final SyncTimer st;

	public SynchronizeTask(SyncTimer st) {
		super("synchronize");
		this.st = st;
	}

	@Override
	@Timed(name="DBSync")
	public void execute(ImmutableMultimap<String, String> params, PrintWriter output) throws Exception {
		try {
			//method um den TimerTask des Timers auszuführen !?!?!?
			st.run();
		} catch (Throwable t) {
			t.printStackTrace(output);
			throw t;
		}
	}

}
