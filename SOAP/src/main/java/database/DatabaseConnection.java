package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
	private Connection db;
	private Statement stat;

	public void openDb() {
		if (db == null) {
			try {
				Class.forName("org.h2.Driver");
				db = DriverManager.getConnection("jdbc:h2:../REST/database/test","test","test");
			} catch (Exception e) {
				System.out.println("Erreur lors de la récupération de la connexion à la BDD. "+e.getMessage());
				System.exit(1);
			}
		}
		if (stat == null) {
			try {
				stat = db.createStatement();
			} catch (SQLException e) {
				System.out.println("Erreur lors de la création d'une requête à la BDD. "+e.getMessage());
				System.exit(1);
			}
		}
	}
	public void closeDb() {
		if (stat != null) {
			try {
				stat.close();
			} catch (SQLException e) {
				System.out.println("Erreur lors de la fermeture de la requête à la BDD. "+e.getMessage());
				System.exit(1);
			}
		}
		if (db != null) {
			try {
				db.close();
			} catch (SQLException e) {
				System.out.println("Erreur lors de la fermeture de la connexion à la BDD. "+e.getMessage());
				System.exit(1);
			}
		}
	}
	public void ecriture(String req) throws SQLException {
		if (stat != null) {
			stat.execute(req);
		}
	}
	public ResultSet lecture(String req) throws SQLException {
		if (stat != null) {
			return stat.executeQuery(req);
		}
		return null;
	}
}
