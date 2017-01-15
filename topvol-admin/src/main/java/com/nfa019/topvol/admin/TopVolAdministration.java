package com.nfa019.topvol.admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TopVolAdministration extends Application {

	@Override
	public void start(final Stage stage) throws Exception {
		final FXMLLoader loaderApp = new FXMLLoader(getClass().getResource("/Application.fxml"));
		stage.setTitle("TopVol - Administration");
		stage.setScene(new Scene(loaderApp.load()));
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
