package net.buchlese.bofc.gui;

import java.sql.Date;
import java.time.YearMonth;
import java.util.Locale;

import com.vaadin.data.Container;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ShiftCalViewImpl extends CustomComponent implements ShiftCalViewIF {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Calendar cal;
	
	public ShiftCalViewImpl() {
		setSizeFull();
		setId("shiftCalComponent");
		final VerticalLayout layout = new VerticalLayout();
		layout.setHeight(100f, Unit.PERCENTAGE);
		
		Label header = new Label("<font size=5><b>Zeiterfassung</b></font>", ContentMode.HTML);
		header.setWidthUndefined();
		layout.addComponent(header);
		layout.setSpacing(true);
		layout.setComponentAlignment(header, Alignment.TOP_CENTER);
		
		cal = new Calendar();
		cal.setLocale(Locale.getDefault());
		
		cal.setStartDate(Date.valueOf(YearMonth.now().atDay(1)));
		cal.setEndDate(Date.valueOf(YearMonth.now().atEndOfMonth()));
		cal.setFirstVisibleHourOfDay(9);;
		cal.setLastVisibleHourOfDay(20);;
		
		layout.addComponent(cal);
		layout.setExpandRatio(cal, 5f);
		layout.setComponentAlignment(cal, Alignment.MIDDLE_CENTER);
		
		setCompositionRoot(layout);
	}
	
	@Override
	public void setDisplay(Container.Indexed c) {
		cal.setContainerDataSource(c);
	}

}
