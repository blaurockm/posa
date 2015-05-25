package net.buchlese.inve;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.h2.tools.Csv;

public class InveCLI {



	public static void main(String args[]) {
		new InveCLI().run();
	}

	private Map<String, Article> articles;
	private Stock s;
	public InveCLI() {
		s = new Stock();
		articles = new HashMap<String,Article>();
	}

	public void run() {
		try {
			loadArticlesFromCsv();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Artikelstamm mit " + articles.size() + " bereit.");
		
		String line = null;
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			line = sc.nextLine().trim();
			if (line.startsWith("#")) {
				continue;
			}
			if (line.equals("stop")) {
				break;
			}
			Article a = articles.get(line);
			if (a != null) {
				// something scanned that we know
				s.add(a, 1);
				System.err.println("-> " + a);
			} else {
				// something scanned we don't know
				System.err.println(" uns fehlt " + line);
			}
		}
		System.err.println("\n\n\n\n\nfertig.\nGesamtwert: " + s.printVK());
		sc.close();
	}

	private void loadArticlesFromCsv() throws SQLException {
		Csv csv = new Csv();
		csv.setFieldSeparatorRead(';');
		ResultSet rs = csv.read("artikelstamm.csv", null, "utf-8");
		rs.next();
//		ResultSetMetaData meta = rs.getMetaData();
		while (rs.next()) {
			Article a = new Article();
			a.setEk(rs.getBigDecimal(6));
			a.setVk(rs.getBigDecimal(7));
			a.setId(rs.getString(1));
			a.setIsbn(rs.getString(2));
			a.setMatchcode(rs.getString(4));
			articles.put(a.getIsbn(), a);
		}
		rs.close();
	}		
}
