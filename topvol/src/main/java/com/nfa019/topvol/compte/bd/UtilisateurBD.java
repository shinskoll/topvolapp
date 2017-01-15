package com.nfa019.topvol.compte.bd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.nfa019.topvol.bd.DatabaseException;
import com.nfa019.topvol.bd.GestionnaireBD;
import com.nfa019.topvol.compte.Administrateur;
import com.nfa019.topvol.compte.Utilisateur;
import com.nfa019.topvol.compte.Utilisateur.RoleEnum;

public class UtilisateurBD {

	private static String TABLE = "UTILISATEUR";

	private static String REQUETE_CREATION = 
			"CREATE TABLE " + TABLE
			+ "(ID IDENTITY, "
			+ "IDENTIFIANT VARCHAR(30), "
			+ "NOM VARCHAR(250), "
			+ "PRENOM VARCHAR(250), "
			+ "MAIL VARCHAR(250), "
			+ "MOT_DE_PASSE VARCHAR(250), "
			+ "ROLE_UTIISATEUR VARCHAR(30))";

	private static String REQUETE_INSERTION = 
			"INSERT INTO " + TABLE + 
			" (IDENTIFIANT, NOM, PRENOM, MAIL, MOT_DE_PASSE, ROLE_UTIISATEUR) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";
	
	private static String REQUETE_MISE_A_JOUR = 
			"UPDATE " + TABLE + 
			" SET IDENTIFIANT=?, NOM=?, PRENOM=?, MAIL=?, MOT_DE_PASSE=?, ROLE_UTIISATEUR=? "
			+ "WHERE ID=?";
	
	private static String REQUETE_SUPPRESSION = 
			"DELETE FROM " + TABLE + 
			" u WHERE u.ID = ?";
	private static String REQUETE_RECHERCHE_PAR_NOM = "SELECT * FROM "+ TABLE + 
			" u WHERE u.NOM = ?";
	private static String REQUETE_RECHERCHE_PAR_IDENTIFIANT = "SELECT * FROM " + TABLE + 
			" u WHERE u.IDENTIFIANT = ?";
	private static String REQUETE_RECHERCHE_TOUT = "SELECT * FROM " + TABLE;


	public UtilisateurBD() {
		try {
			GestionnaireBD.instance().executer(REQUETE_CREATION, new Object[]{});
		} catch(DatabaseException e) {
		}
	}


	public List<Utilisateur> listerUtilisateurs() throws DatabaseException {
		ResultSet resultat = GestionnaireBD.instance().rechercher(REQUETE_RECHERCHE_TOUT, new Object[]{});
		List<Utilisateur> list = extraire(resultat);
		return list;
	}

	public List<Utilisateur> rechercherParNom(String nom) throws DatabaseException {
		ResultSet resultat = GestionnaireBD.instance().rechercher(REQUETE_RECHERCHE_PAR_NOM, new Object[]{nom});
		List<Utilisateur> list = extraire(resultat);
		return list;
	}

	public Utilisateur rechercherParIdentifiant(String identifiant) throws DatabaseException {
		ResultSet resultat = GestionnaireBD.instance().rechercher(REQUETE_RECHERCHE_PAR_IDENTIFIANT, new Object[]{identifiant});
		List<Utilisateur> list = extraire(resultat);
		if(list.size() == 1) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public void ajouter(Utilisateur u) throws DatabaseException, IdentifiantExistantException {
		//vérifier si un utilisateur existe déjà avec le même identifiant
		Utilisateur utilisateurExistant = rechercherParIdentifiant(u.getIdentifiant());
		if(utilisateurExistant != null) {
			throw new IdentifiantExistantException(utilisateurExistant.getIdentifiant());
		}
		GestionnaireBD.instance().inserer(REQUETE_INSERTION, new Object[]{
				u.getIdentifiant(),
				u.getNom(),
				u.getPrenom(),
				u.getMail(),
				u.getMotDePasse(),
				u.getRole()
		});
		Utilisateur utilisateurInsere = rechercherParIdentifiant(u.getIdentifiant());
		if(utilisateurInsere == null) {
			throw new DatabaseException("L'utilisateur n'a pas été ajouté");
		}
		u.setId(utilisateurInsere.getId());
	}
	
	public void mettreAJour(Utilisateur u) throws DatabaseException {
		Utilisateur utilisateurExistant = rechercherParIdentifiant(u.getIdentifiant());
		if(utilisateurExistant == null) {
			throw new DatabaseException("L'utilisateur n'existe pas");
		}
		GestionnaireBD.instance().executer(REQUETE_MISE_A_JOUR, new Object[]{
				u.getIdentifiant(),
				u.getNom(),
				u.getPrenom(),
				u.getMail(),
				u.getMotDePasse(),
				u.getRole(),
				utilisateurExistant.getId()
		});
	}


	public void supprimer(Utilisateur u) throws DatabaseException {
		Utilisateur utilisateurExistant = rechercherParIdentifiant(u.getIdentifiant());
		if(utilisateurExistant == null) {
			throw new DatabaseException("L'utilisateur n'existe pas");
		}
		GestionnaireBD.instance().executer(REQUETE_SUPPRESSION, new Object[]{
				utilisateurExistant.getId()
		});
	}

	List<Utilisateur> extraire(ResultSet resultat) throws DatabaseException {
		List<Utilisateur> list = new ArrayList<Utilisateur>();
		try {
			while(resultat.next()) {
				Integer id = resultat.getInt("ID");
				String identifiant = resultat.getString("IDENTIFIANT");
				String nom = resultat.getString("NOM");
				String prenom = resultat.getString("PRENOM");
				String mail = resultat.getString("MAIL");
				String motDePasse = resultat.getString("MOT_DE_PASSE");
				String roleString = resultat.getString("ROLE_UTIISATEUR");

				Utilisateur u = null;
				RoleEnum role = RoleEnum.valueOf(roleString);
				if(role.equals(RoleEnum.ADMIN)) {
					u = new Administrateur(nom, prenom, mail, identifiant, motDePasse);
				} else {
					u = new Utilisateur(nom, prenom, mail, identifiant, motDePasse);
				}
				u.setId(id);
				list.add(u);
			}
			GestionnaireBD.instance().quietClose(resultat);
			return list;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

}
