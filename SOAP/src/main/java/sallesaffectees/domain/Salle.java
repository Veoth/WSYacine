package sallesaffectees.domain;

public class Salle {
	 private Integer id;
	 private String ville;
	 private Integer nbPlaces;
	 private String nom;
	 
	public Salle(Integer id, String ville, Integer nbPlaces, String nom) {
		this.id = id;
		this.ville = ville;
		this.nbPlaces = nbPlaces;
		this.nom = nom;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public Integer getNbPlaces() {
		return nbPlaces;
	}

	public void setNbPlaces(Integer nbPlaces) {
		this.nbPlaces = nbPlaces;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}	 

}
