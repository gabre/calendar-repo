package model;

import java.sql.*;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class DataManager {

	private Connection conn = null;
	
	public DataManager() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:CalendarDatabase.db;");
		CreateTables();
	}
	
	private void CreateTables() throws SQLException {
		DatabaseMetaData metaData = conn.getMetaData();
		ResultSet tables = metaData.getTables(null, null, "COMPETENCES", new String[] { "TABLE" });
		
        if(!tables.next()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(
                    "CREATE TABLE Competences (" +
                    	"Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Competence VARCHAR(255) UNIQUE" +
                    ")"
                );
                stmt.execute(
                        "CREATE TABLE Resources (" +
                        	"Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "Resource VARCHAR(255)," +
                        	"Competence INTEGER," +
                            "FOREIGN KEY (Competence) REFERENCES Competences(Competence)" +
                        ")"
                    );
            }
        }
        tables.close();
	}
	
	public void addResource(String name, String competence) throws SQLException {
		Statement stm = conn.createStatement();
		ResultSet r = stm.executeQuery("SELECT Id FROM Competences WHERE competence = '" + competence + "'");
		r.next();
		stm.execute("INSERT INTO Resources (Resource, Competence) VALUES('" + name + "', " + r.getInt(1) +")");
	}
	
	public void deleteResource(String name) {
		
	}
	
	public void editResource(String name) {
		
	}
	
	public void addCompetence(String name) throws SQLException {
		Statement stm = conn.createStatement();
		stm.execute("INSERT INTO Competences(Competence) VALUES ('" + name + "')");
	}
	
	public void deleteCompetence(String name) throws SQLException {
		Statement stm = conn.createStatement();
		stm.execute("DELETE FROM Competences WHERE Competence = " + name);
	}
	
	public void editCompetence(String name) {
		
	}
	
	public ObservableList<String> getCompetences() throws SQLException {
		ObservableList<String> result = FXCollections.observableArrayList();
		Statement stm = conn.createStatement();
		ResultSet r = stm.executeQuery("SELECT Competence FROM Competences");
		while(r.next()) {
			result.add(r.getString(1));
		}
		return result;
	}
	
	public ObservableList<ResourceElement> getResources() throws SQLException {
		ObservableList<ResourceElement> result = FXCollections.observableArrayList();
		Statement stm = conn.createStatement();
		ResultSet t = stm.executeQuery("SELECT Resource, Competences.Competence FROM Resources JOIN Competences ON Resources.Competence = Competences.Id");
		
        while(t.next()) {
        	result.add(new ResourceElement(t.getString(1), t.getString(2)));
        }  
		return result;
	}
}

