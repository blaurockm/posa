package net.buchlese.bofc.api.subscr;

public enum ShipType {
	MAIL ("per Post"),
	PUBLISHER ("durch den Verlag"),
	PICKUP ("Abholer"),
	DELIVERY ("Belieferung d. Buchlese");
	
	private String text;
	ShipType(String d) {
		this.text = d;
	}
	
	public String getText() {
		return text;
	}
}
