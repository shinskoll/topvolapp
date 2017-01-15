package com.nfa019.topvol.web.compte;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.nfa019.topvol.bd.DatabaseException;
import com.nfa019.topvol.compte.Utilisateur;
import com.nfa019.topvol.compte.bd.IdentifiantExistantException;
import com.nfa019.topvol.compte.bd.UtilisateurBD;

@Path("/utilisateur")
public class UtilisateurRessource {

	private UtilisateurBD bd = null;
	private boolean initialise = false;

	public UtilisateurRessource() throws DatabaseException {
		this.bd = new UtilisateurBD();
		init();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/lister")
	public List<Utilisateur> listerUtilisateurs() throws DatabaseException {
		return this.bd.listerUtilisateurs();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/ajouter")
	public void ajouterUtilisateur(Utilisateur u) throws DatabaseException, IdentifiantExistantException {
		this.bd.ajouter(u);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/modifier")
	public Response modifierUtilisateur(Utilisateur u) throws DatabaseException {
		try {
			this.bd.mettreAJour(u);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
		return Response.status(200).entity(u).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/login")
	public Response login(Utilisateur u) throws DatabaseException {
		try {
			Utilisateur uDeLaBase = this.bd.rechercherParIdentifiant(u.getIdentifiant());
			if(uDeLaBase != null && uDeLaBase.getMotDePasse().equals(u.getMotDePasse())) {
				return Response.status(200).entity(u).build();
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Status.FORBIDDEN).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/supprimer")
	public Response supprimerUtilisateur(Utilisateur u) throws DatabaseException {
		try {
			this.bd.supprimer(u);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
		return Response.status(200).entity(u).build();
	}

	void init() throws DatabaseException {
		List<Utilisateur> listUtilisateurs = this.bd.listerUtilisateurs();
		if(!initialise && listUtilisateurs.isEmpty()) {
			for(int i=0; i < 5; i++) {
				Utilisateur u = new Utilisateur("u-"+i, "u-"+i, i+"@nfa019.fr", "u-"+i, "utilisateur-"+i);
				try {
					this.bd.ajouter(u);
				} catch (IdentifiantExistantException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
}
