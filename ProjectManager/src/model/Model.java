package model;

import static org.junit.Assert.assertArrayEquals;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Stack;
import java.util.concurrent.Callable;

import model.ModelUtils.Getter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class Model {
	private static final Comparator<CalendarEntry> entryComparator = new Comparator<CalendarEntry>() {
		@Override
		public int compare(CalendarEntry e1, CalendarEntry e2) {
			int c = e1.getDate().compareTo(e2.getDate());
			if (c == 0) {
				return e1.getName().compareTo(e2.getName());
			} else {
				return c;
			}
		}
	};

	private ObservableList<CalendarEntry> entries = FXCollections
			.observableArrayList();
	private ObservableList<Project> projects = FXCollections
			.observableArrayList();
	private ObservableList<ResourceElement> resources = FXCollections
			.observableArrayList();
	private ObservableList<String> competences = FXCollections
			.observableArrayList();

	private Connection connection;

	public Model() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager
				.getConnection("jdbc:sqlite:CalendarDatabase.db;");
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
		return entries;
	}

	public Project sortProjectPlan(GraphDataModel graph)
			throws CycleDetectedException {
		LinkedList<Integer> sortedList = TopologicalSorter
				.topologicalSort(graph.getGraphStructure().toAdjacencyMatrix());
		Project p = new Project("unnamed");
		HashMap<Integer, GraphNodeData> nodes = graph.getNodes();
		Integer[] expectedL = new Integer[] { 1, 3, 2, 4 };
		assertArrayEquals(sortedList.toArray(), expectedL);
		for (Integer nodeId : sortedList) {
			GraphNodeData node = nodes.get(nodeId);
			System.out.println(nodeId);
			if (node == null)
				System.out.println("node null");
			if (node != null && node.desc == null)
				System.out.println("node desc null");
			ProjectStep newStep = new ProjectStep(node.desc);
			p.addStep(newStep);
		}
		projects.add(p);
		return p;
	}

	public HashMap<String, Number> calculateMetrics(Project p) {
		HashMap<String, Number> metrics = new HashMap<>();
		metrics.put("Átlagos idõtartam", calculateAvgDuration(p));
		metrics.put("Teljes idõtartam", calculateSumDuration(p));
		metrics.put("Átlagköltség", calculateAvgCost(p));
		metrics.put("Összköltség", calculateSumCost(p));
		return metrics;
	}

	private int calculateSumDuration(Project p) {
		return calculateSum(p, new ModelUtils.DurationGetter());
	}

	private int calculateSumCost(Project p) {
		return calculateSum(p, new ModelUtils.CostGetter());
	}

	private int calculateSum(Project p, Getter fun) {
		int sum = 0;
		for (ProjectStep item : p.getAllSteps()) {
			sum += fun.getInt(item);
		}
		return sum;
	}

	private double calculateAvgDuration(Project p) {
		int size = p.getAllSteps().size();
		return size == 0 ? 0 : calculateSumDuration(p) / size;
	}

	private double calculateAvgCost(Project p) {
		int size = p.getAllSteps().size();
		return size == 0 ? 0 : calculateSumCost(p) / size;
	}

	public void addProject(Project proj) {
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
		try (Statement s = connection.createStatement()) {
			s.execute("CREATE TABLE IF NOT EXISTS Projects ("
					+ "Id INTEGER PRIMARY KEY," + "Name VARCHAR(255)" + ")");
			s.execute("CREATE TABLE IF NOT EXISTS ProjectSteps ("
					+ "ProjectId INTEGER," + "Name VARCHAR(255),"
					+ "Duration INTEGER," + "Description VARCHAR(255),"
					+ "Difficulty INTEGER," + "NeededCompetence VARCHAR(255),"
					+ "Cost INTEGER" + ")");
			s.execute("CREATE TABLE IF NOT EXISTS Entries ("
					+ "ProjectId INTEGER," + "Name VARCHAR(255),"
					+ "Description VARCHAR(255)," + "Date INTEGER" + ")");
			s.execute("CREATE TABLE IF NOT EXISTS Competences ("
					+ "Id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "Competence VARCHAR(255) UNIQUE" + ")");
			s.execute("CREATE TABLE IF NOT EXISTS Resources ("
					+ "Id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "Resource VARCHAR(255) UNIQUE,"
					+ "Competence INTEGER,"
					+ "FOREIGN KEY (Competence) REFERENCES Competences(Competence)"
					+ ")");

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
					entries.add(new CalendarEntry(res.getString("Name"), res
							.getString("Description"), new Date(res
							.getLong("Date"))));
				} else {
					entries.add(new CalendarEntry(res.getString("Name"), res
							.getString("Description"), new Date(res
							.getLong("Date")), projs.get(res
							.getInt("ProjectId"))));
				}
			}
			
			res = s.executeQuery("SELECT * FROM Resources");
			while (res.next()) {
				ResourceElement r = new ResourceElement(res.getString("Resource"),
														res.getString("Competence"));
				resources.add(r);
			}
			
			res = s.executeQuery("SELECT * FROM Competences");
			while (res.next()) {
				String comp = res.getString("Competence");
				competences.add(comp);
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
			s.execute("DELETE FROM Resources");
			s.execute("DELETE FROM Competences");
			HashMap<Project, Integer> projIds = new HashMap<>();
			int id = 0;
			for (Project proj : projects) {
				s.execute(String.format(
						"INSERT INTO Projects (Id, Name) VALUES (%d, '%s')",
						id, proj.getName()));
				for (ProjectStep step : proj.getAllSteps()) {
					s.execute(String
							.format("INSERT INTO ProjectSteps "
									+ "(ProjectId, Name, Duration, Description, Difficulty, NeededCompetence, Cost) VALUES ("
									+ "%d, '%s', %d, '%s', %d, '%s', %d)", id,
									step.getName(), step.getDuration(),
									step.getDescription(),
									step.getDifficulty(),
									step.getNeededCompetence(), step.getCost()));
				}
				projIds.put(proj, id);
				id++;
			}

			for (CalendarEntry entry : entries) {
				if (entry.getProject() == null) {
					s.execute(String.format(
							"INSERT INTO Entries (ProjectId, Name, Description, Date) VALUES ("
									+ "NULL, '%s', '%s', %d)", entry.getName(),
							entry.getDescription(), entry.getDate().getTime()));
				} else {
					s.execute(String.format(
							"INSERT INTO Entries (ProjectId, Name, Description, Date) VALUES ("
									+ "%d, '%s', '%s', %d)",
							projIds.get(entry.getProject()), entry.getName(),
							entry.getDescription(), entry.getDate().getTime()));
				}
			}
			
			for (ResourceElement res : resources) {
				s.execute(String.format(
						"INSERT INTO Resources (Resource, Competence) VALUES ('%s', '%s')",
						res.getName(), res.getCompetence()));
			}
			
			for (String comp :competences) {
				s.execute(String.format(
						"INSERT INTO Competences (Competence) VALUES ('%s')",
						comp));
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void modifyProjectName(Project sel, String name) {
		sel.setName(name);
		int i = projects.indexOf(sel);
		projects.remove(i);
		projects.add(i, sel);
	}

	public boolean addResource(ResourceElement newRes) {
		boolean l = false;
		for(ResourceElement r : resources) {
			if(r.getName().equals(newRes.getName()))
				l = true;
		}
		if(l)
			return false;
		resources.add(newRes);
		return true;
	}
	
	public void deleteResource(ResourceElement elem) {
		resources.remove(elem);
	}
	
	public void editResource(ResourceElement oldValue, ResourceElement newValue) {
		int index = resources.indexOf(oldValue);
		resources.set(index, newValue);
	}
	
	public boolean addCompetence(String name) {
		if(competences.contains(name))
			return false;
		competences.add(name);
		return true;
	}
	
	public void deleteCompetence(String name) {
		competences.remove(name);
	}
	
	public void editCompetence(String oldName, String newName) {
		int index = competences.indexOf(oldName);
		competences.set(index, newName);
	}
	
	public ObservableList<String> getCompetences() {
		return competences;
	}
	
	public ObservableList<ResourceElement> getResources() {
		return resources;
	}
}
