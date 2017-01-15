package com.nfa019.topvol.admin.compte;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import org.controlsfx.dialog.Dialogs;

import com.nfa019.topvol.admin.api.DataService;
import com.nfa019.topvol.compte.Administrateur;
import com.nfa019.topvol.compte.Utilisateur;
import com.nfa019.topvol.compte.Utilisateur.RoleEnum;


public class UtilisateurControlleur implements Initializable {

	@FXML
	TableView<Utilisateur> tableUtilisateur;
	@FXML 
	TableColumn<Utilisateur, String> colonneIdentifiant;
	@FXML 
	TableColumn<Utilisateur, String> colonneNom;
	@FXML 
	TableColumn<Utilisateur, String> colonnePrenom;
	@FXML 
	TableColumn<Utilisateur, String> colonneMail;
	@FXML 
	TableColumn<Utilisateur, String> colonneMotDePasse;
	@FXML 
	TableColumn<Utilisateur, RoleEnum> colonneRole;
	@FXML
	TextField nouveauIdentifiant;
	@FXML
	TextField nouveauMotDePasse;
	@FXML
	TextField nouveauNom;
	@FXML
	TextField nouveauPrenom;
	@FXML
	TextField nouveauMail;
	@FXML
	ComboBox<RoleEnum> nouveauRole;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		colonneIdentifiant.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("identifiant"));
		colonneNom.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("nom"));
		colonnePrenom.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("prenom"));
		colonneMail.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("mail"));
		colonneMotDePasse.setCellFactory(TextFieldTableCell.<Utilisateur>forTableColumn());
		colonneMotDePasse.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("motDePasse"));
		GestionnaireDeModificationDeMotDePasse editeur = new GestionnaireDeModificationDeMotDePasse();
		colonneMotDePasse.setOnEditCommit(editeur);
		colonneRole.setCellValueFactory(new PropertyValueFactory<Utilisateur, RoleEnum>("role"));
		nouveauRole.getItems().addAll(RoleEnum.values());
		rafraichir();
	}

	public void ajouter() {
		Utilisateur u = null;
		try {
			if(RoleEnum.ADMIN.equals(nouveauRole.getValue())) {
				u = new Administrateur(nouveauNom.getText(), 
						nouveauPrenom.getText(), nouveauMail.getText(), 
						nouveauIdentifiant.getText(), nouveauMotDePasse.getText());
			} else {
				u = new Utilisateur(nouveauNom.getText(), 
						nouveauPrenom.getText(), nouveauMail.getText(), 
						nouveauIdentifiant.getText(), nouveauMotDePasse.getText());
			}
			DataService<Utilisateur> ds = new DataService<Utilisateur>("utilisateur/ajouter");
			ds.post(u);
			viderLesChampsDAjout();
			rafraichir();
		} catch (Exception e) {
			Dialogs.create()
			.title("Erreur")
			.masthead("Ne peut pas ajouter l'utilisateur")
			.showException(e);
		}
	}

	public void supprimer() {
		try {
			Utilisateur u = tableUtilisateur.getSelectionModel()
					.getSelectedItem();
			DataService<Utilisateur> ds = new DataService<Utilisateur>(
					"utilisateur/supprimer");
			ds.post(u);
			rafraichir();
		} catch (Exception e) {
			Dialogs.create().title("Erreur")
			.masthead("Ne peut pas supprimer l'utilisateur")
			.showException(e);
		}
	}


	public void rafraichir() {
		try {
			DataService<Utilisateur> ds = new DataService<Utilisateur>("utilisateur/lister");
			ObservableList<Utilisateur> cu = ds.get(Utilisateur[].class);
			tableUtilisateur.getItems().setAll(cu);
			cu.addListener(new ListChangeListener<Utilisateur>(){
				public void onChanged(Change<? extends Utilisateur> c) {
					tableUtilisateur.getItems().setAll(cu);
				}
			});
		} catch (Exception e) {
			Dialogs.create()
			.title("Erreur")
			.masthead("Ne peut pas récupérer les utilisateurs. Vérifier que le serveur est bien démarré sur localhost:8080/app")
			.showException(e);
		}
	}

	private void viderLesChampsDAjout() {
		nouveauIdentifiant.clear();
		nouveauMail.clear();
		nouveauMotDePasse.clear();
		nouveauNom.clear();
		nouveauPrenom.clear();
		nouveauRole.setValue(null);
	}

	class GestionnaireDeModificationDeMotDePasse implements EventHandler<TableColumn.CellEditEvent<Utilisateur,String>> {

		@Override
		public void handle(CellEditEvent<Utilisateur, String> event) {
			try {
				DataService<Utilisateur> ds = new DataService<Utilisateur>("utilisateur/modifier");
				Utilisateur u = event.getRowValue();
				u.setMotDePasse(event.getNewValue());
				ds.put(u);
				rafraichir();
			} catch (Exception e) {
				Dialogs.create()
				.title("Erreur")
				.masthead("Ne peut pas modifier l'utilisateur")
				.showException(e);
				rafraichir();
			}
		}
	}
}
