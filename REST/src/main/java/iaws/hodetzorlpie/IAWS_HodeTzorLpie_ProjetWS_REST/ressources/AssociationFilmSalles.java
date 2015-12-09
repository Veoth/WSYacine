package iaws.hodetzorlpie.IAWS_HodeTzorLpie_ProjetWS_REST.ressources;

import iaws.hodetzorlpie.IAWS_HodeTzorLpie_ProjetWS_REST.database.DatabaseConnection;
import iaws.hodetzorlpie.IAWS_HodeTzorLpie_ProjetWS_REST.utilitaires.UtilitaireXML;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.POST;
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

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Path("assoc")
public class AssociationFilmSalles {
	private static final String API_URI = "http://www.omdbapi.com/?";
	private final String erreurGenerale = "<creationAssociation>Une erreur est survenue. Cette association existe peut être déjà.</creationAssociation>";
	
	@POST
	@Produces(MediaType.TEXT_XML)
	public String creerAssociation(@QueryParam("filmid") String filmId,@QueryParam("salleid") String salleId) {
		try {
			//création du document XML à retourner
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("creationAssociation");
			doc.appendChild(rootElement);

			//verification de la présence des paramètres
			if (filmId == null || salleId == null) {
				rootElement.appendChild(doc.createTextNode("Les paramètres salleid et filmid sont obligatoires."));
				return UtilitaireXML.getStringFromDocument(doc);
			}

			// Si l'identifiant IMDb est incorrect, stop.
			final Client c = ClientBuilder.newClient();
			final WebTarget wt = c.target(API_URI);
			String text = wt.queryParam("i",filmId).queryParam("r", "xml").request(MediaType.TEXT_XML).get(String.class); 
			if (text.contains("<error>"))
				return text;

			// Insertion du nouveau couple salle/film dans la base de donnée.
			DatabaseConnection db = new DatabaseConnection();
			db.openDb();
			String req = "insert into AFFECTATION (IDSALLE,IMDB) values ('" +salleId+ "', '" +filmId+"')";
			String reqLect = "select * from AFFECTATION where IDSALLE='"+salleId+"' and IMDB='"+filmId+"'";
			ResultSet resLect;
			try {
				db.ecriture(req);
				resLect = db.lecture(reqLect);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				return erreurGenerale;
			}

			//remplissage du document XML à retourner
			Element assoc = doc.createElement("association");
			try {
				resLect.next();
				assoc.setAttribute("idSalle", resLect.getString("IDSALLE"));
				assoc.setAttribute("imdb", resLect.getString("IMDB"));
			} catch (DOMException | SQLException e) {
				
			}
			db.closeDb();
			
			rootElement.appendChild(assoc);
			return UtilitaireXML.getStringFromDocument(doc);
			
		} catch (ParserConfigurationException e1) {
			System.out.println(e1.getMessage());
			return erreurGenerale;
		}
	}
}
