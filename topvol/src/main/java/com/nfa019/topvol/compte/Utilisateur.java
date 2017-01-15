package com.nfa019.topvol.compte;

public class Utilisateur {
	
	private int id;
	private String identifiant;
	private String nom;
	private String prenom;
	protected String mail;
	private String motDePasse;
	
	protected RoleEnum role = RoleEnum.UTILISATEUR;
	
	private static String EMAIL_REGEXP = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

	Utilisateur() {
	}
	
	Utilisateur(String nom, String prenom, String identifiant) {
		this.nom = nom;
		this.prenom = prenom;
		this.identifiant = identifiant;
		if(this.identifiant == null || this.identifiant.isEmpty()) {
			this.identifiant = prenom+"."+nom;
		}
		this.identifiant = this.identifiant.toLowerCase();
	}
	/**
	 * @param nom
	 * @param prenom
	 * @param mail
	 * @param identifiant Si l'identifiant est null ou vide, sa valeur par défaut
	 * est <code>prenom.nom</code>
	 * @throws IllegalArgumentException si le mail n'est pas valide.
	 */
	public Utilisateur(String nom, String prenom, String mail,
			String identifiant, String motDePasse) {
		this(nom, prenom, identifiant);
		setMotDePasse(motDePasse);
		setMail(mail);
	}
	
	void setMail(String mail) {
		if(mail == null) {
			throw new IllegalArgumentException("Le mail de l'utilisateur ne doit pas être vide");
		}
		
		if(!mail.matches(EMAIL_REGEXP)) {
			throw new IllegalArgumentException("Le mail de l'utilisateur n'est pas valide");	
		}
		this.mail = mail;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public String getMail() {
		return mail;
	}
	public String getIdentifiant() {
		return identifiant;
	}
	
	public String getMotDePasse() {
		return motDePasse;
	}
	public void setMotDePasse(String motDePasse) {
		if(motDePasse == null || motDePasse.length() < 8) {
			throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères");
		}
		this.motDePasse = motDePasse;
	}
	
	public RoleEnum getRole() {
		return role;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identifiant == null) ? 0 : identifiant.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Utilisateur other = (Utilisateur) obj;
		if (identifiant == null) {
			if (other.identifiant != null)
				return false;
		} else if (!identifiant.equals(other.identifiant))
			return false;
		return true;
	}
	
	public enum RoleEnum {
		ADMIN,
		UTILISATEUR
	}
}
