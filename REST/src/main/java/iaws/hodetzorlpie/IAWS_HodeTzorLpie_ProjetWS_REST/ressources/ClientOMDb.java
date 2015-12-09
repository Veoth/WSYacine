package iaws.hodetzorlpie.IAWS_HodeTzorLpie_ProjetWS_REST.ressources;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;


@Path("films")
public class ClientOMDb {
	private static final String API_URI = "http://www.omdbapi.com/?";
	
	@GET
    @Produces(MediaType.TEXT_XML)
	public String getListeFilms(@QueryParam("titre") String titre,@QueryParam("annee") Integer annee) {
		final Client c = ClientBuilder.newClient();
		final WebTarget wt = c.target(API_URI);
		String text;
		if (titre != null && annee == null)
			text = wt.queryParam("s",titre).queryParam("r", "xml").request(MediaType.TEXT_XML).get(String.class); 
		else if (titre != null && annee != null)
			text = wt.queryParam("s",titre).queryParam("y", annee).queryParam("r", "xml").request(MediaType.TEXT_XML).get(String.class);
		else
			text = new String("<root/>");
	
		return text;
	}
}
