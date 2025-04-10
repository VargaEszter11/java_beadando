package beadando;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DbKonyvKezelo {
	
	private Statement s = null;
	private Connection conn = null;
	private ResultSet rs = null;

	
	public void message(final String msg, final int type) {
		JOptionPane.showMessageDialog(null, msg, "Program üzenet", type);
	}
 
	public void message(final String msg) {
		System.out.println(msg);
	}
	
	public void registration() {
		System.out.println("hali");
		try {
			Class.forName("org.sqlite.JDBC");
			message("Sikeres driver regisztráció!");
		} catch (ClassNotFoundException e) {
			message("Hibás driver regisztráció!" + e.getMessage(), 0);
		}
	}
	
	public void connect() {
		try {
			String url = "jdbc:sqlite:/home/eszter/sqlite/booksdb";
			conn = DriverManager.getConnection(url);
			message("Connection OK!");
		} catch (SQLException e){
			message("JDBC connect: " + e.getMessage(), 0);
		}
	}

	public void disconnect() {

		try {

			conn.close();

			message("Disconnected!");

		} catch (SQLException e){

			message("JDBC disconnect: " + e.getMessage(), 0);

		}		

	}
}
