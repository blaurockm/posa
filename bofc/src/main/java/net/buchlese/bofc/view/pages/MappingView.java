package net.buchlese.bofc.view.pages;

import java.util.List;

import net.buchlese.bofc.api.bofc.Mapping;
import net.buchlese.bofc.view.AbstractBofcView;

public class MappingView extends AbstractBofcView {
	private final List<Mapping> mappings;
	
	public MappingView(List<Mapping> ae) {
		super("mappings.ftl");
		this.mappings = ae;
	}

	public List<Mapping> getMappings() {
		return mappings;
	}
	

}
