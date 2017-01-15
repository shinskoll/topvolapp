package com.nfa019.topvol.compte;

import org.junit.Assert;
import org.junit.Test;

public class AdministrateurTest {
	
	@Test
	public void setMail_doitModifierLeMail() {
		Administrateur a = new Administrateur("Dupont", "Jack", "a@a.fr", "j.dupont", "12345678");
		Assert.assertEquals("a@a.fr", a.getMail());
		a.setMail("new@new.fr");
		Assert.assertEquals("new@new.fr", a.getMail());
	}

}
