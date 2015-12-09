package iaws.hodetzorlpie.IAWS_HodeTzorLpie_ProjetWS_REST.ressources;

import java.sql.ResultSet;
import java.sql.SQLException;

import iaws.hodetzorlpie.IAWS_HodeTzorLpie_ProjetWS_REST.database.DatabaseConnection;
import iaws.hodetzorlpie.IAWS_HodeTzorLpie_ProjetWS_REST.utilitaires.UtilitaireXML;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Path("salles")
public class Salles {
	
	private static final String API_URI = "http://www.omdbapi.com/?";
	private final String errorParams = "<root>L'identifiant IMDb doit être saisie.</root>";
	private final String dbErrRead = "<root>Problème lors de la lecture de la base de donnée</root>";
	private final String erreurGenerale = "<listeSalles>Une erreur est survenue. Cette association existe peut être déjà.</listeSalles>";
	
	@GET
    @Produces(MediaType.TEXT_XML)
	@Path("criteres")
	public String rechercheSalles(@QueryParam("id") String id,@QueryParam("nom") String nom,@QueryParam("ville") String ville,@QueryParam("nbplace") Integer nbPlace) {
		try {
			//création du document XML à retourner
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("listeSalles");
			doc.appendChild(rootElement);
		
			DatabaseConnection db = new DatabaseConnection();
			db.openDb();
			
			//création de la requête SQL
			int cptReq = 0;
			String req = "Select ID,NOM,VILLE,NBPLACE from SALLE where ";
			
			if (id != null) {
				req += "id='"+id+"' ";
				cptReq++;
			}
			if (nom != null) {
				if (cptReq > 0) 
					req += "and ";
				cptReq++;
				req += "nom='"+nom+"' ";
			}
			if (ville != null) {
				if (cptReq > 0) 
					req += "and ";
				cptReq++;
				req += "ville='"+ville+"' ";
			}
			if (nbPlace != null) {
				if (cptReq > 0) 
					req += "and ";
				cptReq++;
				req += "nbplace='"+nbPlace+"' ";
			}
	
			//exécution de la requête et remplissage du document XML à retourner
			if (cptReq > 0) {
				try {
					ResultSet resReq = db.lecture(req);
	
					if (resReq != null) {
						while (resReq.next()) {
							Element salleElem = doc.createElement("salle");
							salleElem.setAttribute("id",resReq.getString("ID"));
							salleElem.setAttribute("nom", resReq.getString("NOM"));
							salleElem.setAttribute("ville", resReq.getString("VILLE"));
							salleElem.setAttribute("nbPlace", resReq.getString("NBPLACE"));
							rootElement.appendChild(salleElem);
						}
					}
				} catch (SQLException e) {
					return erreurGenerale;
				}
			}
		
			db.closeDb();
			return UtilitaireXML.getStringFromDocument(doc);
			} catch (ParserConfigurationException e1) {
				System.out.println(e1.getMessage());
				return erreurGenerale;
			}
	}
	
	@GET
    @Produces(MediaType.TEXT_XML)
	@Path("parFilm")
	public String getSallesByFilm(@QueryParam("filmid") String filmId) {
		try {
			//création du document XML à retourner
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("listeSalles");
			rootElement.setAttribute("idFilm", filmId);
			doc.appendChild(rootElement);
			
			//contrôle de la présence du paramètre
			if (filmId == null) 
				return errorParams;
			
			// Si l'identifiant IMDb est incorrect, stop.
			final Client c = ClientBuilder.newClient();
			final WebTarget wt = c.target(API_URI);
			String text = wt.queryParam("i",filmId).queryParam("r", "xml").request(MediaType.TEXT_XML).get(String.class); 
			if (text.contains("<error>"))
				return text;
	
			//préparation de la requête SQL
			DatabaseConnection db = new DatabaseConnection();	
			db.openDb();
			String req = "select * from SALLE where ID in (select IDSALLE from AFFECTATION where IMDB='" + filmId + "')";
			
			//exécution de la requête et remplissage du document XML à retourner
			try {
				ResultSet resReq = db.lecture(req);
	
				if (resReq != null) {
					while (resReq.next()) {
						Element salleElem = doc.createElement("salle");
						salleElem.setAttribute("id",resReq.getString("ID"));
						salleElem.setAttribute("nom", resReq.getString("NOM"));
						salleElem.setAttribute("ville", resReq.getString("VILLE"));
						salleElem.setAttribute("nbPlace", resReq.getString("NBPLACE"));
						rootElement.appendChild(salleElem);
					}
				}
			} catch (SQLException e) {
				return dbErrRead;
			}
			
			db.closeDb();
			return UtilitaireXML.getStringFromDocument(doc);
		} catch (ParserConfigurationException e1) {
			System.out.println(e1.getMessage());
			return erreurGenerale;
		}
	}
}
