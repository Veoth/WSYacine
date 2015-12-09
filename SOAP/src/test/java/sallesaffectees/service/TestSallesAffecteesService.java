package sallesaffectees.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import sallesaffectees.domain.Salle;

public class TestSallesAffecteesService {
	
	@Test
	public void testSallesAffectees() {
        SallesAffecteesServiceImpl sallesAffecteesServcice = new SallesAffecteesServiceImpl();
        List<Salle> salles = sallesAffecteesServcice.trouverSallesAffectesAUnIDFilm("tt1201607");
        
        assertEquals("Test au moins une salles est renvoyé", salles.isEmpty(), false);
        assertEquals("Test que la ville de la salle est bien maVille", salles.get(0).getVille(), "maVille");
    }
}
