package com.nfa019.topvol.compte.bd;

import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.nfa019.topvol.compte.Administrateur;
import com.nfa019.topvol.compte.Utilisateur;

public class UtilisateurBDTest {

	@Test
	public void testAjouterUtilisateur() throws Exception {
		UtilisateurBD bd = new UtilisateurBD();
		String identifiant = genererChaineDeCaracteresUnique();
		Utilisateur u = new Utilisateur("Dupont", "Jack", "mail@nfa019.com", identifiant, "12345678"); 
		bd.ajouter(u);
		Assert.assertNotEquals("L'utilisateur ajouté n'a pas d'ID !", u.getId(), 0);
	}
	
	@Test
		public void testListerUtilisateurs() throws Exception {
			UtilisateurBD bd = new UtilisateurBD();
			List<Utilisateur> listeAvant = bd.listerUtilisateurs();
			String identifiant = genererChaineDeCaracteresUnique();
			Utilisateur u = new Utilisateur("Dupont", "Jack", "mail@nfa019.com", identifiant, "12345678"); 
			bd.ajouter(u);
			List<Utilisateur> listApres = bd.listerUtilisateurs();
			Assert.assertEquals("L'utilisateur n'a pas été ajouté !", listeAvant.size()+1, listApres.size());
			Assert.assertTrue("L'utilisateur ajouté n'est pas trouvé !", listApres.contains(u));
			
		}

	@Test
	public void testRechercherParNom() throws Exception {
		String identifiant = genererChaineDeCaracteresUnique();
		String nom = genererChaineDeCaracteresUnique();
		Utilisateur u = new Utilisateur(nom, "Jack", "mail@nfa019.com", identifiant, "12345678"); 
		UtilisateurBD bd = new UtilisateurBD();
		bd.ajouter(u);
		List<Utilisateur> list = bd.rechercherParNom(nom);
		Assert.assertEquals("L'utilisateur n'a pas été ajouté !", 1, list.size());
		Assert.assertTrue("L'utilisateur ajouté n'est pas trouvé !", list.contains(u));
	}

	@Test
	public void testRechercherParIdentifiant() throws Exception {
		String identifiant = genererChaineDeCaracteresUnique();
		String nom = genererChaineDeCaracteresUnique();
		Utilisateur u = new Utilisateur(nom, "Jack", "mail@nfa019.com", identifiant, "12345678"); 
		UtilisateurBD bd = new UtilisateurBD();
		bd.ajouter(u);
		Utilisateur ur = bd.rechercherParIdentifiant(identifiant);
		Assert.assertNotNull("L'objet utilisateur est null", ur);
		Assert.assertEquals("L'objet utilisateur crée n'est pas celui retourné par la recherche", u, ur);
	}
	
	@Test
	public void testAjouterAdministrateur() throws Exception {
		UtilisateurBD bd = new UtilisateurBD();
		String identifiant = genererChaineDeCaracteresUnique();
		Administrateur u = new Administrateur("Dupont", "Jack", "mail@nfa019.com", identifiant, "12345678"); 
		bd.ajouter(u);
		Utilisateur inserer = bd.rechercherParIdentifiant(u.getIdentifiant());
		Assert.assertTrue("L'utilisateur ajouté n'est pas un Administrateur !", inserer instanceof Administrateur);
	}
	
	
	@Test
	public void testSupprimerUtilisateur() throws Exception {
		UtilisateurBD bd = new UtilisateurBD();
		String identifiant = genererChaineDeCaracteresUnique();
		Utilisateur u = new Utilisateur("Dupont", "Jack", "mail@nfa019.com", identifiant, "12345678"); 
		bd.ajouter(u);
		Utilisateur insere = bd.rechercherParIdentifiant(u.getIdentifiant());
		Assert.assertNotNull("L'objet utilisateur est null", insere);
		Assert.assertEquals("L'objet utilisateur crée n'est pas celui retourné par la recherche", u, insere);
		bd.supprimer(u);
		Utilisateur neDoitPasExister = bd.rechercherParIdentifiant(u.getIdentifiant());
		Assert.assertNull("L'objet utilisateur n'a pas été supprimé", neDoitPasExister);
	}
	
	@Test
	public void testMettreAJourUtilisateur() throws Exception {
		UtilisateurBD bd = new UtilisateurBD();
		String identifiant = genererChaineDeCaracteresUnique();
		Administrateur u = new Administrateur("Dupont", "Jack", "mail@nfa019.com", identifiant, "12345678"); 
		bd.ajouter(u);
		Utilisateur insere = bd.rechercherParIdentifiant(u.getIdentifiant());
		Assert.assertNotNull("L'objet utilisateur est null", insere);
		Assert.assertEquals("L'objet utilisateur crée n'est pas celui retourné par la recherche", u, insere);
		
		u.setMotDePasse("newmdp12345678");
		u.setMail("newmail@mail.com");
		
		bd.mettreAJour(u);
		Utilisateur utilisateurModife = bd.rechercherParIdentifiant(u.getIdentifiant());
		Assert.assertEquals("Le mot de passe n'a pas été modifié", u.getMotDePasse(), utilisateurModife.getMotDePasse());
		Assert.assertEquals("Le mail n'a pas été modifié", u.getMail(), utilisateurModife.getMail());
	}
	
	private String genererChaineDeCaracteresUnique() {
		Random r = new Random();
		String chaine = "chaine-" + r.nextInt() + "-" + System.currentTimeMillis();
		return chaine;
	}
 }
