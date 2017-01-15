package com.nfa019.topvol.compte.bd;

public class IdentifiantExistantException extends Exception {

	private static final long serialVersionUID = -6053425557788993617L;
	
	private String identifiant;
	
	public IdentifiantExistantException(String identifiant) {
		this.identifiant = identifiant;
	}
	
	public String getIdentifiant() {
		return identifiant;
	}
}
