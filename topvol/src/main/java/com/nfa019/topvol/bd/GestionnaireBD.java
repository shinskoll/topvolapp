package com.nfa019.topvol.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class GestionnaireBD {

	//Le nom de la classe du pilote JDBC
	private static String driverClass = "org.hsqldb.jdbcDriver"; 

	private String jdbcUrl;

	//L'utilisateur et le mot de passe par défaut de HSQLDB
	private String user          = "sa";
	private String password      = "";

	private Properties properties;
	
	private static GestionnaireBD instance = null;


	GestionnaireBD(String nomDeBaseDeDonnees) throws DatabaseException {
		//L'adresse de la connection à la base de données
		this.jdbcUrl= "jdbc:hsqldb:mem:"+nomDeBaseDeDonnees+";create=true";
		try {
			Class.forName(driverClass).newInstance();
			properties = new Properties();

			properties.put("user", user);
			properties.put("password", password);
			properties.put("jdbc.strict_md", "false");
			properties.put("jdbc.get_column_name", "false");

		} catch (InstantiationException e) {
			throw new DatabaseException(e);
		} catch (IllegalAccessException e) {
			throw new DatabaseException(e);
		} catch (ClassNotFoundException e) {
			throw new DatabaseException(e);
		}
	}

	public static GestionnaireBD instance() throws DatabaseException {
		if(instance == null) {
			instance = new GestionnaireBD();
		}
		return instance;
	}
	
	private GestionnaireBD() throws DatabaseException {
		this("nfa019");
	}

	public Connection creerConnection() throws DatabaseException {
		try {
			Connection conn = DriverManager.getConnection(jdbcUrl, properties);
			conn.setAutoCommit(true);
			return conn;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public void inserer(String requeteSQLInsertion, Object[] parameters) throws DatabaseException {
		Connection connection = creerConnection();
		PreparedStatement insertion = null;
		try {
			insertion = connection.prepareStatement(requeteSQLInsertion);
			for (int index=0; index < parameters.length;index++) {
				insertion.setObject(index+1, parameters[index]);
			}
			insertion.execute();
		} catch(SQLException exp) {
			throw new DatabaseException(exp);
		} finally {
			quietClose(insertion);
			quietClose(connection);
		}
	}

	public ResultSet rechercher(String requeteSQLRecherche, Object[] parameters) throws DatabaseException {
		Connection connection = creerConnection();
		PreparedStatement recherche = null;
		try {
			recherche = connection.prepareStatement(requeteSQLRecherche);
			if(parameters != null) {
				for (int index=0; index < parameters.length;index++) {
					recherche.setObject(index+1, parameters[index]);
				}
			}
			ResultSet resultat = recherche.executeQuery();
			return resultat;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		} finally {
			quietClose(recherche);
			quietClose(connection);
		}
	}

	public void executer(String sql, Object[] parameters) throws DatabaseException {
		Connection connection = creerConnection();
		PreparedStatement recherche = null;
		try {
			recherche = connection.prepareStatement(sql);
			if(parameters != null) {
				for (int index=0; index < parameters.length;index++) {
					recherche.setObject(index+1, parameters[index]);
				}
			}
			recherche.execute();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		} finally {
			quietClose(recherche);
			quietClose(connection);
		}
	}

	public void quietClose(Connection connection) {
		try {
			if(connection != null) { 
				connection.close();
			}
		} catch(SQLException exp) {
			exp.printStackTrace();
		}
	}

	public void quietClose(Statement statement) {
		try {
			if(statement != null) {
				statement.close();
			}
		} catch(SQLException exp) {
			exp.printStackTrace();
		}
	}

	public void quietClose(ResultSet resultSet) {
		try {
			resultSet.close();
		} catch(SQLException exp) {
			exp.printStackTrace();
		}
	}

}
