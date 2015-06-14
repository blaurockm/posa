package net.buchlese.bofc.gui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import net.buchlese.bofc.api.bofc.PosCashBalance;

import com.vaadin.data.Container;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.data.Property;

public class VoucherViewImpl extends CustomComponent implements VoucherViewIF  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Table table;
	public VoucherViewImpl() {
		setSizeFull();
		setId("voucherView");
		final VerticalLayout layout = new VerticalLayout();
		layout.setHeight(100f, Unit.PERCENTAGE);
		
//		Label header = new Label("Kassenberichte");
//		header.setSizeUndefined();
//		layout.addComponent(header);
//		layout.setComponentAlignment(header,  Alignment.TOP_CENTER);
		
		table = new Table("Kassenberichte") {
			@Override
		    protected String formatPropertyValue(Object rowId,   Object colId, Property property) {
		        // Format by property type
		        if (property.getType() == PosCashBalance.class && property.getValue() != null) {
		            NumberFormat currFormat = DecimalFormat.getCurrencyInstance();
					return currFormat.format(((PosCashBalance)property.getValue()).getRevenue() /100d ) + " / " +
					currFormat.format(((PosCashBalance)property.getValue()).getAbsorption() /100d );
		        }

		        return super.formatPropertyValue(rowId, colId, property);
		    }
		};;

		// Define two columns for the built-in container
		table.setPageLength(7);
		
		table.setFooterVisible(true);
		table.setColumnFooter("Wochentag", "Summe");
		table.setColumnFooter("Datum", null);
		table.setColumnFooter("Kasse Sulz", null);
		table.setColumnFooter("Kasse Dornhan", null);

		layout.addComponent(table);
		layout.setComponentAlignment(table,  Alignment.TOP_CENTER);
		setCompositionRoot(layout);
	}

	public void setDisplay(Container c) {
		table.setContainerDataSource(c);
		
	}

	@Override
	public void setDisplayFooter(String sumSulz, String sumDornhan) {
		table.setColumnFooter("Kasse Sulz", sumSulz);
		table.setColumnFooter("Kasse Dornhan", sumDornhan);
	}
	
	
}
