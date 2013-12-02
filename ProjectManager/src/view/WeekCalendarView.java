package view;

import java.text.DateFormat;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class WeekCalendarView extends GridPane {
	private static final String[] shortDayNames = new String[] {null, "v", "h", "k", "sze", "cs", "p", "szo"};
	
	private ProjectManagerApplication app;
	private int weeks;
	private Calendar firstDay;
	
	private VBox dayBoxes[];
	private Label dayLabels[];
	private Label yearLabel = new Label();
	
	public WeekCalendarView(final ProjectManagerApplication app, int weeks) {
		super();
		this.app = app;
		this.weeks = weeks;
		
		dayBoxes = new VBox[7 * weeks];
		dayLabels = new Label[7 * weeks];
		
		app.getModel().getCalendarEntries().addListener(new ListChangeListener<CalendarEntry>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends CalendarEntry> event) {
				addEntries();
			}
		});
		firstDay = calculateFirstDay();
		
		add(yearLabel, 0, 0);
		yearLabel.getStyleClass().add("calendar-year-label");
		for (int i = 0; i < 7; ++i) {
			ColumnConstraints cc = new ColumnConstraints();
			cc.setHgrow(Priority.ALWAYS);
			cc.setPercentWidth(100.0 / 7.0);
			getColumnConstraints().add(cc);
			
			for (int j = 0; j < weeks; ++j) {
				final int k = i + 7 * j;
				
				dayLabels[k] = new Label();
				dayLabels[k].getStyleClass().add("calendar-day-label");
				add(dayLabels[k], i, 2 * j + 1);
				
				dayBoxes[k] = new VBox(5);
				dayBoxes[k].getStyleClass().add("calendar-day");
				add(dayBoxes[k], i, 2 * j + 2);
				
				dayBoxes[k].setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (event.getButton().equals(MouseButton.PRIMARY)) {
							Date date = new Date(firstDay.getTimeInMillis() + k * 1000 * 86400);
							CalendarEntry entry = new CalendarEntry("Új esemény", "", date);
							app.getModel().addEntry(entry);
							app.calendarEntryOpened(entry);
						}
					}
				});
			}
		}
		
		getRowConstraints().add(new RowConstraints());
		for (int i = 0; i < weeks; ++i) {
			RowConstraints rc = new RowConstraints();
			rc.setVgrow(Priority.ALWAYS);
			getRowConstraints().addAll(new RowConstraints(), rc);
		}
		
		setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (event.getDeltaY() < 0) {
					firstDay.add(Calendar.DATE, 7);
					addEntries();
				} else if (event.getDeltaY() > 0) {
					firstDay.add(Calendar.DATE, -7);
					addEntries();
				}
			}	
		});
		
		addEntries();
		
		setHgap(5);
	}
	
	private void addEntries() {
		yearLabel.setText(Integer.toString(firstDay.get(Calendar.YEAR)));
		for (VBox box : dayBoxes) {
			box.getChildren().clear();
		}
		for (final CalendarEntry entry : app.getModel().getCalendarEntries()) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(entry.getDate());
			long diff = (cal.getTimeInMillis() - firstDay.getTimeInMillis()) / (1000 * 86400);
			if (diff >= 0 && diff < 7 * weeks) {
				Label lab = new Label(entry.getName());
				lab.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (event.getButton() == MouseButton.PRIMARY) {
							app.calendarEntryOpened(entry);
							event.consume();
						}
					}	
				});
				lab.getStyleClass().add("calendar-event");
				lab.setContextMenu(new CalendarEntryContextMenu(app, entry));
				dayBoxes[(int) diff].getChildren().add(lab);
			}
		}
		Calendar cal = (Calendar) firstDay.clone();
		for (Label lab : dayLabels) {
			lab.setText(String.format("%02d. %02d., %s",
					cal.get(Calendar.MONTH) + 1,
					cal.get(Calendar.DAY_OF_MONTH),
					shortDayNames[cal.get(Calendar.DAY_OF_WEEK)]));
			cal.add(Calendar.DATE, 1);
		}
	}
	
	private Calendar calculateFirstDay() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal;
	}

}
