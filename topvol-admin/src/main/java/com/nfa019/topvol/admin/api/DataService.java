package com.nfa019.topvol.admin.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.jackson.JacksonFeature;

public class DataService<T> {

	public static String MACHINE_PORT = "http://localhost:8080"; 
	public static String URL = MACHINE_PORT + "/";
	
	private URL serviceUrl;
	
	public ObservableList<T> get(final Class<T[]> type) throws JsonParseException, JsonMappingException, IOException {
		ObservableList<T> rv = FXCollections.observableArrayList();
		ObjectMapper m = new ObjectMapper();
		T[] resultat = m.readValue(serviceUrl.openStream(), type);
		rv.addAll(resultat);
		return rv;
	}
	
	@SuppressWarnings("unchecked")
	public T post(T object) {
		Client client = ClientBuilder.newClient()
				.register(JacksonFeature.class);
		return (T) client.target(serviceUrl.toString())
				.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(object, MediaType.APPLICATION_JSON), object.getClass());
	}
	
	@SuppressWarnings("unchecked")
	public T put(T object) {
		Client client = ClientBuilder.newClient()
				.register(JacksonFeature.class);
		return (T) client.target(serviceUrl.toString())
				.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(object, MediaType.APPLICATION_JSON), object.getClass());
	}
	

	public DataService(String nomDuService) {
		URL url = null;
		try {
			url = new URL(String.format("%s%s", URL, nomDuService));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		this.serviceUrl = url;
		
	}
	
	
}
