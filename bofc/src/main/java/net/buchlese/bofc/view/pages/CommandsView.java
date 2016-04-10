package net.buchlese.bofc.view.pages;

import io.dropwizard.views.View;

import java.util.Collection;

import net.buchlese.bofc.BackOfcConfiguration;
import net.buchlese.bofc.api.cmd.AbstractBofcCommand;
import net.buchlese.bofc.resources.CommandResource;

public class CommandsView extends View {

	private final BackOfcConfiguration cfg;
	private final CommandResource res;
	
	public CommandsView(BackOfcConfiguration config, CommandResource rs) {
		super("commands.ftl");
		this.cfg = config;
		this.res = rs;
	}

	public String getPosName() {
		cfg.getMetricsFactory();
		return "Backend";
	}
	
	public Collection<AbstractBofcCommand> getCmdsToDo() {
		return res.fetchCmdToDo();
		
	}

	public Collection<AbstractBofcCommand> getCmdsDone() {
		return res.fetchCmdDone();
		
	}

}
