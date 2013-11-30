package view;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import model.CalendarEntry;
import app.ProjectManagerApplication;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class WeekCalendarView extends GridPane {
	private ProjectManagerApplication app;
	private Calendar firstDay;
	
	private VBox dayBoxes[] = new VBox[7];
	
	public WeekCalendarView(final ProjectManagerApplication app) {
		super();
		this.app = app;
		app.getModel().getCalendarEntries().addListener(new ListChangeListener<CalendarEntry>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends CalendarEntry> event) {
				addEntries();
			}
		});
		firstDay = firstDayOfCurrentWeek();
		
		String dayNames[] = {"Hétfõ", "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombat", "Vasárnap"};
		for (int i = 0; i < dayNames.length; ++i) {
			ColumnConstraints cc = new ColumnConstraints();
			cc.setHgrow(Priority.ALWAYS);
			cc.setPercentWidth(100.0 / 7.0);
			getColumnConstraints().add(cc);
			add(new Label(dayNames[i]), i, 0);
			dayBoxes[i] = new VBox(5);
			dayBoxes[i].setPadding(new Insets(2, 5, 2, 5));
			dayBoxes[i].setMinWidth(50);
			dayBoxes[i].setStyle("-fx-border-color: black; -fx-border-width: 1;");
			add(dayBoxes[i], i, 1);
			
			final int j = i;
			dayBoxes[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (event.getButton().equals(MouseButton.PRIMARY)) {
						Date date = new Date(firstDay.getTimeInMillis() + j * 1000 * 86400);
						CalendarEntry entry = new CalendarEntry("Új esemény", "", date);
						app.getModel().addEntry(entry);
						app.calendarEntryOpened(entry);
					}
				}
			});
		}
		RowConstraints rc = new RowConstraints();
		rc.setVgrow(Priority.ALWAYS);
		getRowConstraints().addAll(new RowConstraints(), rc);
		
		addEntries();
		
		setHgap(5);
	}
	
	private void addEntries() {
		for (VBox box : dayBoxes) {
			box.getChildren().clear();
		}
		for (final CalendarEntry entry : app.getModel().getCalendarEntries()) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(entry.getDate());
			long diff = (cal.getTimeInMillis() - firstDay.getTimeInMillis()) / (1000 * 86400);
			if (diff >= 0 && diff < 7) {
				Label lab = new Label(entry.getName());
				lab.setContextMenu(new CalendarEntryContextMenu(app, entry));
				dayBoxes[(int) diff].getChildren().add(lab);
			}
		}
	}
	
	private Calendar firstDayOfCurrentWeek() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal;
	}

}
