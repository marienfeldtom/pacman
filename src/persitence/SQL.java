package persitence;

import java.sql.*;

public class SQL {

	static java.sql.Connection con = null;
	static final String adress = "jdbc:mysql://localhost:3306/pacman";
	static final String pw = "";
	static final String user_name = "root";
	static final String table = "bestenliste";

	public static void send(String name, int points, String level) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(adress, user_name, pw);
			Statement st = (Statement) con.createStatement();
			st.executeUpdate("INSERT INTO " + table + " (name, wert, level) VALUES ('" + name + "', '" + points
					+ "', '" + level + "');");
			con.close();
		}

		catch (SQLException ex) {
			System.out.println(ex);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String[][] receiveAll(String level) {
		String[][] list = null;

		try {

			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(adress, user_name, pw);

			String query = "SELECT * FROM " + table + " WHERE level='" + level + "' ORDER BY wert DESC;";

			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery(query);
			int max = 0;
			rs.last();
			max = rs.getRow();
			rs.beforeFirst();

			list = new String[max][];

			while (rs.next()) {

				String name = rs.getString("name");
				String wert = rs.getString("wert");
				String[] eintrag = { name, wert };
				list[rs.getRow() - 1] = eintrag;
			}
			st.close();
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e);
		}

		return list;
	}

}
