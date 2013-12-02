package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class Model {
	private static final Comparator<CalendarEntry> entryComparator = new Comparator<CalendarEntry>() {
		@Override
		public int compare(CalendarEntry e1, CalendarEntry e2) {
			return e1.getDate().compareTo(e2.getDate());
		}
	};
	
	private ObservableList<CalendarEntry> entries = FXCollections.observableArrayList();
	private ObservableList<Project> projects = FXCollections.observableArrayList();
	
	private Connection connection;
	
	public Model() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:CalendarDatabase.db;");
		createTables();
		loadFromDb();
	}
	
	public void addEntry(CalendarEntry e) {
		entries.add(e);
		FXCollections.sort(entries, entryComparator);
	}
	
	public void removeEntry(CalendarEntry e) {
		entries.remove(e);
	}
	
	public void modifyEntry(CalendarEntry from, CalendarEntry to) {
		if (entries.remove(from)) {
			entries.add(to);
			FXCollections.sort(entries, entryComparator);
		}
	}

	public ObservableList<CalendarEntry> getCalendarEntries() {
		return FXCollections.unmodifiableObservableList(entries);
	}
	
	public void sortProjectPlan()
	{
		
	}
	
	public HashMap<String, Number> calculateMetrics(Project p)
	{
		HashMap<String, Number> metrics = new HashMap<>();
		metrics.put("Average duration", calculateAvgDuration(p));
		metrics.put("Sum of duration", calculateSumDuration(p));
		return metrics;
	}
	
	private int calculateSumDuration(Project p) {
		int sum = 0;
		for(ProjectStep item : p.getAllSteps())
		{
			sum += item.getDuration();
		}
		return sum;
	}

	private double calculateAvgDuration(Project p) {
		int size = p.getAllSteps().size();
		return size == 0 ? 0 : calculateSumDuration(p) / size;
	}

	public void addProject(Project proj)
	{
		projects.add(proj);
	}
	
	public void removeProject(Project proj) {
		Iterator<CalendarEntry> iter = entries.iterator();
		while (iter.hasNext()) {
			CalendarEntry entry = iter.next();
			if (entry.getProject() == proj) {
				iter.remove();
			}
		}
		
		proj.getAllSteps().clear();
		projects.remove(proj);
	}
	
	public ObservableList<Project> getProjects() {
		return projects;
	}
	
	private void createTables() {
		try {
			Statement s = connection.createStatement();
			s.execute(
					"CREATE TABLE IF NOT EXISTS Projects (" +
						"Id INTEGER PRIMARY KEY," +
						"Name VARCHAR(255)" +
					")"
					);
			s.execute(
					"CREATE TABLE IF NOT EXISTS ProjectSteps (" +
						"ProjectId INTEGER," +
						"Name VARCHAR(255)," +
						"Duration INTEGER," +
						"Description VARCHAR(255)," +
						"Difficulty INTEGER," +
						"NeededCompetence VARCHAR(255)," +
						"Cost INTEGER" +
					")"
					);
			s.execute(
					"CREATE TABLE IF NOT EXISTS Entries (" +
						"ProjectId INTEGER," +
						"Name VARCHAR(255)," +
						"Description VARCHAR(255)," +
						"Date INTEGER" +
					")"
					);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	private void loadFromDb() {
		try {
			Statement s = connection.createStatement();
			ResultSet res = s.executeQuery("SELECT Id, Name FROM Projects");
			HashMap<Integer, Project> projs = new HashMap<>();
			while (res.next()) {
				Project proj = new Project(res.getString("Name"));
				projects.add(proj);
				projs.put(res.getInt("Id"), proj);
			}
			
			res = s.executeQuery("SELECT * FROM ProjectSteps");
			while (res.next()) {
				Project proj = projs.get(res.getInt("ProjectId"));
				ProjectStep step = new ProjectStep();
				step.setName(res.getString("Name"));
				step.setDuration(res.getInt("Duration"));
				step.setDescription(res.getString("Description"));
				step.setDifficulty(res.getInt("Difficulty"));
				step.setNeededCompetence(res.getString("NeededCompetence"));
				step.setCost(res.getInt("Cost"));
				proj.addStep(step);
			}
			
			res = s.executeQuery("SELECT ProjectId, Name, Description, Date FROM Entries");
			while (res.next()) {
				if (res.getObject("ProjectId") == null) {
					entries.add(new CalendarEntry(res.getString("Name"),
							                      res.getString("Description"),
							                      new Date(res.getLong("Date"))));
				} else {
					entries.add(new CalendarEntry(res.getString("Name"),
		                                          res.getString("Description"),
		                                          new Date(res.getLong("Date")),
		                                          projs.get(res.getInt("ProjectId"))));
				}
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void saveToDb() {
		try {
			Statement s = connection.createStatement();
			s.execute("DELETE FROM Projects");
			s.execute("DELETE FROM ProjectSteps");
			s.execute("DELETE FROM Entries");
			HashMap<Project, Integer> projIds = new HashMap<>();
			int id = 0;
			for (Project proj : projects) {
				s.execute(String.format("INSERT INTO Projects (Id, Name) VALUES (%d, '%s')",
						                id, proj.getName()));
				for (ProjectStep step : proj.getAllSteps()) {
					s.execute(String.format("INSERT INTO ProjectSteps " +
				                            "(ProjectId, Name, Duration, Description, Difficulty, NeededCompetence, Cost) VALUES (" +
							                "%d, '%s', %d, '%s', %d, '%s', %d)",
							                id,
							                step.getName(),
							                step.getDuration(),
							                step.getDescription(),
							                step.getDifficulty(),
							                step.getNeededCompetence(),
							                step.getCost()));
				}
				projIds.put(proj, id);
				id++;
			}
			
			for (CalendarEntry entry : entries) {
				if (entry.getProject() == null) {
					s.execute(String.format("INSERT INTO Entries (ProjectId, Name, Description, Date) VALUES (" +
				                            "NULL, '%s', '%s', %d)",
				                            entry.getName(),
				                            entry.getDescription(),
				                            entry.getDate().getTime()));
				} else {
					s.execute(String.format("INSERT INTO Entries (ProjectId, Name, Description, Date) VALUES (" +
                            "%d, '%s', '%s', %d)",
                            projIds.get(entry.getProject()),
                            entry.getName(),
                            entry.getDescription(),
                            entry.getDate().getTime()));
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void modifyProjectName(Project sel, String name) {
		// TODO Auto-generated method stub
		Project p = sel.clone();
		p.setName(name);
		projects.set(projects.indexOf(sel), p);
	}
	
}
