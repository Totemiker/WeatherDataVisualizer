/*
 * Copyright (c) 2019 Tobias Teumert
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import data.model.Area;
import data.model.Reading;
import data.model.Reading.LevelType;
import data.model.StationId;
import data.provider.DataProvider;
import gui.util.PlotCommand;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Controller of Main_GUI
 * 
 * @author Tobias
 *
 */
public class ChartGUIController {

	
	/** The Dataprovider to get Data from*/
	private DataProvider provider;

	@FXML
	private MenuItem menuNewChart;

	/**
	 * Shows the available Areas to the user Holds the chosen Area
	 */
	@FXML
	private ComboBox<Area> choiceRegion;

	/**
	 * Shows the available Stations of the chosen Area Holds the chosen Station
	 */
	@FXML
	private ComboBox<StationId> choiceStation;

	/**
	 * Startdate of timeinterval from which data is to be pulled
	 */
	@FXML
	private DatePicker pickerStartDate;

	/**
	 * Enddate of timeinterval from which data is to be pulled
	 */
	@FXML
	private DatePicker pickerEndDate;

	/**
	 * Checks wether 12 o'clock data or midnight data should be pulled
	 */
	@FXML
	private CheckBox checkBoxTime;

	/**
	 * Color of the corresponding Series in the Chart
	 */
	@FXML
	private ColorPicker colorPickerSeriesColor;

	/**
	 * optional Name for Series
	 */
	@FXML
	private TextField textfieldSeriesName;

	/**
	 * Which Value should be plotted
	 */
	@FXML
	private ComboBox<PlotCommand> choiceValue;

	/**
	 * which level should be plotted
	 */
	@FXML
	private ComboBox<LevelType> choiceLevel;

	/**
	 * Creates a new Series to Display in the active Chart tab
	 */
	@FXML
	private Button buttonAddSeries, buttonCancel;

	/**
	 * The Tab Pane that holds multiple Series. Can have Multiple Charts each in a
	 * new Tab new Tab via Menu -> Loads from FXML
	 */
	@FXML
	private TabPane tabPaneCharts;

	/** Progress Bar of Reading in of Data-Series */
	@FXML
	private ProgressBar progress;

	/** Status of reading in */
	@FXML
	private Label labelProgress;

	/** ListView of read-in Series */
	@FXML
	private ListView<Series<String, Double>> listViewSeries;

	private final ObjectProperty<ObservableList<StationId>> stationIds;
	private final ObjectProperty<ObservableList<Area>> areas;

	private final ObjectProperty<LocalDate> startDate, endDate;

	private final ObjectProperty<Area> selectedArea;
	private final ObjectProperty<StationId> selectedStationId;
	/** 12=true 0=false */
	private final BooleanProperty time;

	private final ObjectProperty<ObservableList<PlotCommand>> plottableValues;
	private final ObjectProperty<PlotCommand> valueToPlot;

	private final ObjectProperty<LevelType> levelToPlot;
	private final ObjectProperty<ObservableList<LevelType>> plottableLevels;

	private final ObjectProperty<Color> pickedColour;
	
	Supplier<ListCell<PlotCommand>> plotCommandListCellFactory = () ->  new ListCell<>() {
		@Override
		protected void updateItem(PlotCommand item, boolean empty) {
			super.updateItem(item, empty);

			if (item == null || empty) {
				setText(null);
			} else {
				setText(item.getDisplayName());
			}
		};
	};
	
	public ChartGUIController() {
		
		
		stationIds = new SimpleObjectProperty<>(this, "stations", FXCollections.observableArrayList());
		areas = new SimpleObjectProperty<>(this, "areas", FXCollections.observableArrayList());
		startDate = new SimpleObjectProperty<>(this, "startDate", LocalDate.now());
		endDate = new SimpleObjectProperty<>(this, "endDate", LocalDate.now());

		selectedArea = new SimpleObjectProperty<>(this, "selectedArea");
		selectedStationId = new SimpleObjectProperty<>(this, "selectedStation");

		valueToPlot = new SimpleObjectProperty<>(this, "selectedPlotCommand");
	
		plottableValues = new SimpleObjectProperty<>(this, "plotCommand",
				FXCollections.unmodifiableObservableList(FXCollections.observableArrayList()));

		levelToPlot = new SimpleObjectProperty<>(this, "selectedLevel");
		
		plottableLevels = new SimpleObjectProperty<>(this, "levelsSelection", FXCollections.observableArrayList());

		time = new SimpleBooleanProperty(this, "time", false);
		pickedColour = new SimpleObjectProperty<>(this, "picked Colour");

		// chartSerien = new SimpleObjectProperty<>(this, "serien");

	}

	/**
	 * Autocalled Method - initialized by FXMLLoader
	 */
	@FXML
	public void initialize() {
		// TODO Bindings CellFactorys
		// TODO Listview Implementation

		choiceStation.itemsProperty().bind(stationIdsProperty());
		choiceRegion.itemsProperty().bind(areaProperty());
		choiceLevel.itemsProperty().bind(plottableLevelsProperty());
		choiceValue.itemsProperty().bind(plottableValuesProperty());

		levelToPlotProperty().bind(choiceLevel.valueProperty());
		valueToPlotProperty().bind(choiceValue.valueProperty());
		selectedStationIdProperty().bind(choiceStation.valueProperty());

		pickerStartDate.valueProperty().bindBidirectional(startDateProperty());
		pickerEndDate.valueProperty().bindBidirectional(endDateProperty());
		checkBoxTime.selectedProperty().bindBidirectional(timeProperty());
		colorPickerSeriesColor.valueProperty().bindBidirectional(pickedColourProperty());

		buttonAddSeries.disableProperty()
				.bind(Bindings.not(Bindings.createBooleanBinding(this::checkInputValidity,
						choiceStation.getSelectionModel().selectedItemProperty(),
						choiceRegion.getSelectionModel().selectedItemProperty(), pickerStartDate.valueProperty(),
						pickerEndDate.valueProperty(), choiceLevel.getSelectionModel().selectedItemProperty(),
						choiceValue.getSelectionModel().selectedItemProperty(), timeProperty(),
						colorPickerSeriesColor.valueProperty())));

		tabPaneCharts.getTabs().add(createTab());
		tabPaneCharts.requestFocus();

		tabPaneCharts.getSelectionModel().selectedItemProperty()
				.addListener((ov, oldTab, newTab) -> System.out.println("changed"));

		// listViewSeries.itemsProperty().bind(null);

		// TODO css für GUI

		initializeDataPresentation();
		initializeTooltips();
	}
	
	/**
	 * Method to supply Tooltips to all appropriate GUI-Elements
	 */
	private void initializeTooltips() {		
		
		choiceRegion.setTooltip(new Tooltip("Choose the desired geographical area"));
		choiceRegion.getTooltip().setShowDelay(Duration.millis(250));
		choiceRegion.getTooltip().setHideDelay(Duration.millis(100));
		
		choiceStation.setTooltip(new Tooltip("Choose the desired station"));
		choiceStation.getTooltip().setShowDelay(Duration.millis(250));
		choiceStation.getTooltip().setHideDelay(Duration.millis(100));
		
		choiceValue.setTooltip(new Tooltip("Choose the displayed parameter"));
		choiceValue.getTooltip().setShowDelay(Duration.millis(250));
		choiceValue.getTooltip().setHideDelay(Duration.millis(100));
		
		choiceLevel.setTooltip(new Tooltip("Choose the desired pressure level"));
		choiceLevel.getTooltip().setShowDelay(Duration.millis(250));
		choiceLevel.getTooltip().setHideDelay(Duration.millis(100));
		
		buttonAddSeries.setTooltip(new Tooltip("Add new line to chart with given parameters"));
		buttonAddSeries.getTooltip().setShowDelay(Duration.millis(250));
		buttonAddSeries.getTooltip().setHideDelay(Duration.millis(100));
		
		checkBoxTime.setTooltip(new Tooltip("Check this, if 12:00 data should be used"));
		checkBoxTime.getTooltip().setShowDelay(Duration.millis(250));
		checkBoxTime.getTooltip().setHideDelay(Duration.millis(100));
		
		buttonCancel.setTooltip(new Tooltip("Cancel data processing"));
		buttonCancel.getTooltip().setShowDelay(Duration.millis(250));
		buttonCancel.getTooltip().setHideDelay(Duration.millis(100));
		
		pickerEndDate.setTooltip(new Tooltip("End of desired time interval"));
		pickerEndDate.getTooltip().setShowDelay(Duration.millis(250));
		pickerEndDate.getTooltip().setHideDelay(Duration.millis(100));
		
		pickerStartDate.setTooltip(new Tooltip("Start of desired time interval"));
		pickerStartDate.getTooltip().setShowDelay(Duration.millis(250));
		pickerStartDate.getTooltip().setHideDelay(Duration.millis(100));
	}

	/**
	 * Initialises all GUI-Elements with the appropriate data factories
	 */
	private void initializeDataPresentation() {
		choiceRegion.setCellFactory(value -> new ListCell<>() {
			@Override
			protected void updateItem(Area item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {setText(null);} 
				else {
					setText(item.getAreaName());
				}
			};
		});

		choiceRegion.setButtonCell(new ListCell<>() {
			@Override
			protected void updateItem(Area item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.getAreaName());
				}
			};
		});

		choiceStation.setCellFactory(value -> new ListCell<>() {
			@Override
			protected void updateItem(StationId item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.getStationName());
				}
			};
		});

		choiceStation.setButtonCell(new ListCell<>() {
			@Override
			protected void updateItem(StationId item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.getStationName());
				}
			};
		});

		choiceLevel.setCellFactory(value -> this.buildLevelTypeListCell());
		choiceLevel.setButtonCell(buildLevelTypeListCell());
		
		choiceValue.setCellFactory(value -> plotCommandListCellFactory.get());
		choiceValue.setButtonCell(plotCommandListCellFactory.get());
	}
	
	private ListCell<LevelType> buildLevelTypeListCell() {
		return new ListCell<LevelType>() {
			@Override
			protected void updateItem(LevelType item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.toString());
				}
			};
		};
	}

	/**
	 * Sets the data provider of the Program
	 * 
	 * @param provider The data provider from where to get Data
	 */
	public void setDataProvider(DataProvider provider) {
		this.provider = provider;
		setAreas(provider.getAreas());
		setPlottableValues(getAvailableSoundingValues());
		setPlottableLevels(getAvailablePressureLevels());
	}
	
	/**
	 * Injected Method from FXML - Buttonclick "Add Series to chart"
	 * @param event The thrown ActionEvent from the GUI
	 */
	@FXML
	private void buttonActionAddSeries(ActionEvent event) {
		
		Color seriesColour = colorPickerSeriesColor.getValue();

		System.out.println("Add Series to active Chart");

		if (tabPaneCharts.getSelectionModel().getSelectedItem() == null)
			tabPaneCharts.getTabs().add(createTab());

		ChartTabController ctc;
		ctc = (ChartTabController) tabPaneCharts.getSelectionModel().getSelectedItem().getUserData();
		
		//Task to decouple JavaFX Thread from Data getting
		
		Task<Series<Number, Double>> task = new Task<Series<Number, Double>>() {
			private Series<Number, Double> series = new Series<>();
			AtomicInteger current = new AtomicInteger(0);

			@Override
			protected Series<Number, Double> call() throws Exception {
				updateMessage("Running...");
				long max = getStartDate().datesUntil(getEndDate().plusDays(1)).count();
				series.setName(getSelectedStationId().getStationName());
				series.setData(getStartDate().datesUntil(getEndDate().plusDays(1))
						.map(date -> new XYChart.Data<Number, Double>(date.toEpochDay(),
								getValueToPlot().execute(provider.getReading(getSelectedStationId(),
										LocalDateTime.of(date, getTime() ? LocalTime.of(12, 0) : LocalTime.of(0, 0)),
										getLevelToPlot()).orElse(getDummy(LevelType.CUSTOM)))))
						.takeWhile(data -> !isCancelled()).peek(data -> updateProgress(current.incrementAndGet(), max))
						.collect(Collectors.toCollection(FXCollections::observableArrayList)));
				
				return series;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				updateMessage("Success!");
				ctc.addSeries(series, seriesColour);
			}

			@Override
			protected void cancelled() {
				super.cancelled();
				updateMessage("Cancelled!");
			}
		};
		progress.progressProperty().bind(task.progressProperty());
		progress.setUserData(task);
		labelProgress.textProperty().bind(task.messageProperty());

		ExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.submit(task);
	}

	/**
	 * Repopulates the Stationlist when Region is switched
	 * 
	 * @param event The thrown ActionEvent from the GUI
	 */
	@SuppressWarnings("unchecked")
	@FXML
	private void choiceActionRegion(ActionEvent event) {
		setStationIds(provider.getStationsByArea(((ComboBox<Area>) event.getSource()).getValue()));
	}

	/**
	 * Adds a new Tab with a new Chart to the TabPane tabPaneCharts
	 * 
	 * @param event The thrown ActionEvent from the GUI
	 */
	@FXML
	private void menuActionNewChart(ActionEvent event) {
		System.out.println("New Chart");
		tabPaneCharts.getTabs().add(createTab());

	}

	/**
	 * Cancels the running import task
	 * 
	 * @param event The thrown ActionEvent from the GUI
	 */
	@SuppressWarnings("unchecked")
	@FXML
	private void buttonActionCancel(ActionEvent event) {
		((Task<Series<String, Double>>) progress.getUserData()).cancel();
	}

	/**
	 * Creates a new Tab from FXML and sets controller
	 * 
	 * @return The created Tab
	 */
	private Tab createTab() {
		try {
			Tab newTab = new Tab("Chart " + (tabPaneCharts.getTabs().size() + 1));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ChartTab.fxml"));
			newTab.setContent(loader.load());
			ChartTabController ctcontrol = loader.getController();
			newTab.setUserData(ctcontrol);

			return newTab;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Checks if all conditions of adding a new Series are fulfilled
	 * 
	 * @return validity of adding a new Series
	 */
	public boolean checkInputValidity() {
		// FIXME check edge cases
		boolean isValid = true;

		isValid &= !(choiceRegion.getValue() == null);
		isValid &= !(choiceStation.getValue() == null);
		isValid &= !(choiceLevel.getValue() == null);
		isValid &= !(choiceValue.getValue() == null);
		isValid &= !(pickerStartDate.getValue() == null);
		isValid &= !(pickerEndDate.getValue() == null);
		// isValid &= !(colorPickerSeriesColor.getValue() == null);
		

		if (isValid) {
			isValid &= !(pickerStartDate.getValue().isAfter(pickerEndDate.getValue()));
			isValid &= !(pickerStartDate.getValue().isAfter(LocalDate.now()));
			isValid &= !(pickerEndDate.getValue().isAfter(LocalDate.now()));
			isValid &= !(pickerStartDate.getValue().isEqual(pickerEndDate.getValue()) && getTime()
					&& LocalTime.now().isBefore(LocalTime.of(12, 00)));
		}
		return isValid;
	}

	/**
	 * Creates a List of available meterological values
	 * @return A List of available values
	 */
	public List<PlotCommand> getAvailableSoundingValues() {
		List<PlotCommand> values = new ArrayList<PlotCommand>();

		values.add(new PlotCommand("Temperature", Reading::getTemp));
		values.add(new PlotCommand("Dew Point", Reading::getDewPoint));
		values.add(new PlotCommand("Wind Direction", Reading::getDirection));
		values.add(new PlotCommand("Wind Speed", Reading::getWindspeed));
		values.add(new PlotCommand("Height", Reading::getHeight));
		values.add(new PlotCommand("Theta", Reading::getTheta));
		values.add(new PlotCommand("Mix", Reading::getMix));

		return values;
	}

	/**
	 * List of available pressure levels
	 * @return A List of available Pressure Levels to Plot
	 */
	public List<LevelType> getAvailablePressureLevels() {
		return List.of(LevelType.values());
	}

	private Reading getDummy(LevelType type) {
		return new Reading(type, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	public Property<ObservableList<LevelType>> plottableLevelsProperty() {
		return plottableLevels;
	}

	public void setPlottableLevels(List<LevelType> dlist) {
		if (dlist instanceof ObservableList<?>)
			plottableLevels.set((ObservableList<LevelType>) dlist);
		else
			plottableLevels.set(FXCollections.observableArrayList(dlist));
	}

	public ObservableList<LevelType> getPlottableLevels() {
		return plottableLevels.get();
	}

	public Property<LevelType> levelToPlotProperty() {
		return levelToPlot;
	}

	public void setLevelToPlot(LevelType doub) {
		levelToPlot.set(doub);
	}

	public LevelType getLevelToPlot() {
		return levelToPlot.get();
	}

	public Property<PlotCommand> valueToPlotProperty() {
		return valueToPlot;
	}

	public void setValueToPlot(PlotCommand plc) {
		valueToPlot.set(plc);
	}

	public PlotCommand getValueToPlot() {
		return valueToPlot.get();
	}

	public Property<ObservableList<PlotCommand>> plottableValuesProperty() {
		return plottableValues;
	}

	public void setPlottableValues(List<PlotCommand> plcom) {
		if (plcom instanceof ObservableList<?>)
			plottableValues.set((ObservableList<PlotCommand>) plcom);
		else
			plottableValues.set(FXCollections.observableArrayList(plcom));
	}

	public ObservableList<PlotCommand> getPlottableValues() {
		return plottableValues.get();
	}

	public Property<ObservableList<StationId>> stationIdsProperty() {
		return stationIds;
	}

	public ObservableList<StationId> getStationIds() {
		return stationIds.get();
	}

	public void setStationIds(List<StationId> st) {
		if (st instanceof ObservableList<?>)
			stationIds.set((ObservableList<StationId>) st);
		else
			stationIds.set(FXCollections.observableArrayList(st));
	}

	public Property<ObservableList<Area>> areaProperty() {
		return areas;
	}

	public ObservableList<Area> getAreas() {
		return areas.get();
	}

	public void setAreas(List<Area> area) {
		if (area instanceof ObservableList<?>)
			areas.set((ObservableList<Area>) area);
		else
			areas.set(FXCollections.observableArrayList(area));
	}

	public Property<LocalDate> startDateProperty() {
		return startDate;
	}

	public Property<LocalDate> endDateProperty() {
		return endDate;
	}

	public LocalDate getStartDate() {
		return startDate.get();
	}

	public LocalDate getEndDate() {
		return endDate.get();
	}

	public void setStartDate(LocalDate date) {
		startDate.set(date);
	}

	public void setEndDate(LocalDate date) {
		endDate.set(date);
	}

	/**
	 * @return the selectedAreaProperty
	 */
	public Property<Area> selectedAreaProperty() {
		return selectedArea;
	}

	/**
	 * @return the selectedStationProperty
	 */
	public Property<StationId> selectedStationIdProperty() {
		return selectedStationId;
	}

	public Area getSelectedArea() {
		return selectedArea.get();
	}

	public StationId getSelectedStationId() {
		return selectedStationId.get();
	}

	public void setSelectedStationId(StationId station) {
		selectedStationId.set(station);
	}

	public void setSelectedArea(Area area) {
		selectedArea.set(area);
	}

	public Property<Boolean> timeProperty() {
		return time;
	}

	public boolean getTime() {
		return time.get();
	}
	/**
	 * Sets the time Property true = 12, false = 0
	 * @param 
	 */
	public void setTime(boolean arg) {
		time.set(arg);
	}
	/**
	 * Property Field pickedColour
	 * @return The pickeColourProperty
	 */
	public Property<Color> pickedColourProperty() {
		return pickedColour;
	}

	public Color getPickedColour() {
		return pickedColour.get();
	}

	public void setPickedColour(Color arg) {
		pickedColour.set(arg);
	}

}
