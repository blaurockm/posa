package net.buchlese.posa.resources;

import io.dropwizard.servlets.tasks.Task;

import java.io.PrintWriter;

import org.joda.time.LocalDate;

import net.buchlese.posa.core.SyncTimer;
import net.buchlese.posa.core.SyncTimer.BulkLoadDetails;

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
		BulkLoadDetails det = new BulkLoadDetails();
		det.setFrom(LocalDate.parse(params.get("from").iterator().next()));
		if (params.containsKey("till")) {
			det.setTill(LocalDate.parse(params.get("till").iterator().next()));
		} else {
			det.setTill(new LocalDate());
		}
		if (params.containsKey("sendHome")) {
			det.setSendHome(Boolean.parseBoolean(params.get("sendHome").iterator().next()));
		} else {
			det.setSendHome(false); 
		}
		st.setBulkLoad(det);
	}

}
