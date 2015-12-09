package iaws.hodetzorlpie.IAWS_HodeTzorLpie_ProjetWS_REST;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AssociationFilmSallesTest {
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
	public void testCreerAssociation() {
		Form form = new Form();
		form.param("filmid", "tt1201607");
		form.param("salleid","1");
		String responseMsg = target.path("assoc").queryParam("filmid", "tt1201607").queryParam("salleid", "1").request().post(Entity.form(form),String.class);
		assertEquals(""
				+ "<creationAssociation>"
				+ "Une erreur est survenue. Cette association existe peut être déjà."
				+ "</creationAssociation>",responseMsg);
	}

}
