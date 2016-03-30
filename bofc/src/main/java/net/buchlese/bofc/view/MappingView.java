package net.buchlese.bofc.view;

import java.util.List;

import net.buchlese.bofc.api.bofc.Mapping;

public class MappingView extends AbstractBofcView {
	private final List<Mapping> mappings;
	
	public MappingView(List<Mapping> ae) {
		super("mapping.ftl");
		this.mappings = ae;
	}

	public List<Mapping> getMappings() {
		return mappings;
	}

	
	

}
