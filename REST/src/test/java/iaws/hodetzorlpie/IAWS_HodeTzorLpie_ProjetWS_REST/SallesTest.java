package iaws.hodetzorlpie.IAWS_HodeTzorLpie_ProjetWS_REST;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SallesTest {
	
	 private HttpServer server;
	 private WebTarget target;

	@Before
	public void setUp() throws Exception {
		// start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        target = c.target(Main.BASE_URI);
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	@Test
	public void testRechercheSalles() {
		String responseMsg = target.path("salles/criteres").queryParam("id", 1).request().get(String.class);
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<listeSalles>"
				+ "<salle id=\"1\" nbPlace=\"5\" nom=\"test\" ville=\"maVille\"/>"
				+ "</listeSalles>",responseMsg);
	}
	
	@Test
	public void testGetSallesByFilm() {
		String responseMsg = target.path("salles/parFilm").queryParam("filmid", "tt1201607").request().get(String.class);
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<listeSalles idFilm=\"tt1201607\">"
				+ "<salle id=\"1\" nbPlace=\"5\" nom=\"test\" ville=\"maVille\"/>"
				+ "</listeSalles>",responseMsg);
	}

}
