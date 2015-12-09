package sallesaffectees.contractfirst;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.server.endpoint.annotation.XPathParam;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sallesaffectees.domain.Salle;
import sallesaffectees.service.SallesAffecteesService;


@Endpoint
public class SallesAffecteesEndpoint {
	
	private SallesAffecteesService sallesAffecteesService;
	
	private static final String NAMESPACE_URI = "http://iaws/projetWS/sallesAffectees";
	
	public SallesAffecteesEndpoint(SallesAffecteesService sallesAffecteesService){
		this.sallesAffecteesService = sallesAffecteesService;
	}
	
	@PayloadRoot(localPart = "SallesAffecteesRequest", namespace = NAMESPACE_URI)
	@Namespace(uri=NAMESPACE_URI, prefix="sa")
	@ResponsePayload
	public Element handleSallesAffecteesRequest(@XPathParam("/sa:SallesAffecteesRequest/sa:idFilm/text()") String idFilm) throws Exception {
		
		
		List<Salle> salles = sallesAffecteesService.trouverSallesAffectesAUnIDFilm(idFilm);
		
		//création du document XML à retourner
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("SallesAffectees");
		rootElement.setAttribute("xmlns", "http://iaws/projetWS/sallesAffectees");
		rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		rootElement.setAttribute("xsi:schemaLocation", "http://iaws/projetWS/sallesAffectees SallesAffectees.xsd");
		rootElement.setAttribute("idFilm", idFilm);
		doc.appendChild(rootElement);
		
		for (Salle s : salles) {
			Element salleElem = doc.createElement("salle");
			salleElem.setAttribute("id", s.getId().toString());
			salleElem.setAttribute("nom", s.getNom());
			salleElem.setAttribute("ville", s.getVille());
			salleElem.setAttribute("nbPlaces", s.getNbPlaces().toString());
			rootElement.appendChild(salleElem);
		}
		
		return rootElement;
	}

}
