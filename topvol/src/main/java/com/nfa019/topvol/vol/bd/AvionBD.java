package com.nfa019.topvol.vol.bd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.nfa019.topvol.bd.DatabaseException;
import com.nfa019.topvol.bd.GestionnaireBD;
import com.nfa019.topvol.compte.Administrateur;
import com.nfa019.topvol.compte.Utilisateur;
import com.nfa019.topvol.compte.Utilisateur.RoleEnum;
import com.nfa019.topvol.compte.bd.IdentifiantExistantException;

public class AvionBD {
	private static String TABLE = "AVION";

	private static String REQUETE_CREATION = 
			"CREATE TABLE " + TABLE
			+ "(ID IDENTITY, "
			+ "IDENTIFIANT VARCHAR(30), "
			+ "DEPART VARCHAR(250), "
			+ "DATEDEPART TIMESTAMP, "
			+ "ARRIVEE VARCHAR(250), "
			+ "DATERETOUR TIMESTAMP, "
			+ "PRIX INTEGER)";
	
	private static String REQUETE_INSERTION = 
			"INSERT INTO " + TABLE + 
			" (IDENTIFIANT, DEPART, DATEDEPART, ARRIVEE, DATERETOUR, PRIX) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";
	
	private static String REQUETE_RECHERCHE_PAR_IDENTIFIANT = "SELECT * FROM " + TABLE + 
			" u WHERE u.IDENTIFIANT = ?";
	private static String REQUETE_RECHERCHE_TOUT = "SELECT * FROM " + TABLE;
	
	public AvionBD(){
		try {
			GestionnaireBD.instance().executer(REQUETE_CREATION, new Object[]{});
		} catch(DatabaseException e) {}
	}
	
	
	public List<Utilisateur> listerUtilisateurs() throws DatabaseException {
		ResultSet resultat = GestionnaireBD.instance().rechercher(REQUETE_RECHERCHE_TOUT, new Object[]{});
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
