package net.buchlese.bofc.gui;

import net.buchlese.bofc.core.statistic.StatisticModel;

public class StatisticPresenter {
	private final StatisticViewIF view;
	private final StatisticModel model;
	
	public StatisticPresenter(StatisticViewIF view,
			StatisticModel model) {
		this.view = view;
		this.model = model;
	}

	public StatisticViewIF getView() {
		return view;
	}

	public StatisticModel getModel() {
		return model;
	}

}
