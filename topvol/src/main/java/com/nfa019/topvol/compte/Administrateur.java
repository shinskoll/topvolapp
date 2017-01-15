package com.nfa019.topvol.compte;

public class Administrateur extends Utilisateur {

	Administrateur() {
	}
	
	public Administrateur(String nom, String prenom, String mail,
			String identifiant, String motDePasse) {
		super(nom, prenom, mail, identifiant, motDePasse);
		role = RoleEnum.ADMIN;
	}
	
	public void setMail(String mail) {
		super.setMail(mail);
	}
}
