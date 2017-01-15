package com.nfa019.topvol.compte;

import org.junit.Assert;
import org.junit.Test;

public class UtilisateurTest {
	
	@Test(expected=IllegalArgumentException.class)
	public void setMail_argNull() {
		Utilisateur u = new Utilisateur("Dupont", "Jack", "j.dupont");
		u.setMail(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void setMail_doitEnvoyerExceptionPourUnMailInvalide() {
		Utilisateur u = new Utilisateur("Dupont", "Jack", "j.dupont");
		u.setMail("a@a");
	}
	
	@Test
	public void setMail_doitPasserPourUnMailValide() {
		Utilisateur u = new Utilisateur("Dupont", "Jack", "j.dupont");
		u.setMail("a@a.fr");
		Assert.assertEquals("a@a.fr", u.getMail());
	}
	
	@Test
	public void ctor_doitAvoirIdentifiantParDefaut() {
		Utilisateur u = new Utilisateur("Dupont", "Jack", "a@a.fr", null, "12345678");
		Assert.assertEquals("jack.dupont", u.getIdentifiant());
	}
	
	@Test
	public void setMotDePasse_doitContenirAuMoins8Caractères() {
		Utilisateur u = new Utilisateur("Dupont", "Jack", "a@a.fr", "jd", "12345678");
		Assert.assertEquals("12345678", u.getMotDePasse());
		try {
			u.setMotDePasse("123");
			Assert.fail("Doit lever une exception");
		} catch(IllegalArgumentException exp) {
		}
	}
}
