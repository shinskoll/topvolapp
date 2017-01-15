package com.nfa019.topvol.web;

import java.io.IOException;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

/**
 * Cette classe permet démarrer un serveur Web accessible sur l'adresse
 * http://localhost:8080/ par défaut.
 * @author adil
 *
 */
public class TopVolServeurWeb 
{
	public static void main( String[] args ) throws IllegalArgumentException, IOException
	{
		String URL = "http://localhost:8080";
		ResourceConfig rc = new PackagesResourceConfig(TopVolServeurWeb.class.getPackage().getName());
		rc.getFeatures().put("com.sun.jersey.api.json.POJOMappingFeature", true);

		HttpServer server = GrizzlyServerFactory.createHttpServer(URL, rc);
		server.getServerConfiguration().addHttpHandler(
				new StaticHttpHandler("src/main/webapp"), "/app", "/static");
		server.getListener("grizzly").getFileCache().setEnabled(false);
		server.start();


		System.out.println("Serveur démarré. Accéder au serveur via votre navigateur web: "+URL+"/app");
		System.in.read();
	}
}	
