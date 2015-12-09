package sallesaffectees.service;

import java.util.List;

import sallesaffectees.domain.Salle;

public interface SallesAffecteesService {
	
    /**
     * Retourne toutes les salles affectées à un film identifié par son ID
     * @param idFilm l'ID IMDb du film
     * @return les salles correspondant à l'id fourni
     */
	public List<Salle> trouverSallesAffectesAUnIDFilm(String idFilm);
}
