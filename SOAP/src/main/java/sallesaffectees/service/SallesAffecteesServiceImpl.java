package sallesaffectees.service;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import sallesaffectees.domain.Salle;

public class SallesAffecteesServiceImpl implements SallesAffecteesService {

	public List<Salle> trouverSallesAffectesAUnIDFilm(String idFilm) {
		List<Salle> sallesList = new ArrayList<Salle>();
		DatabaseConnection db = new DatabaseConnection();
		
		db.openDb();
		
		String req = "select * from SALLE where ID in (select IDSALLE from AFFECTATION where IMDB='" + idFilm + "')";
		
		try {
			ResultSet resReq = db.lecture(req);

			if (resReq != null) {
				while (resReq.next()) {
					Salle salle = new Salle(resReq.getInt("ID"),resReq.getString("VILLE"),resReq.getInt("NBPLACE"),resReq.getString("NOM"));
					sallesList.add(salle);
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		db.closeDb();
		
		return sallesList;
	}

}
