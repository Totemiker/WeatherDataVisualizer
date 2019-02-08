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
import javafx.scene.paint.Color;

/**
 * Controller Klasse der neuen Oberfläche
 * @author Tobias
 *
 */
public class ChartGUIController {
	
	//private WorksheetController controller;
	
	private DataProvider provider;
	
	@FXML
    private MenuItem menuNewChart;
	
	/**
	 * Shows the available Areas to the user
	 * Holds the chosen Area
	 */
	@FXML
    private ComboBox<Area> choiceRegion;
	
	/**
	 * Shows the available Stations of the chosen Area
	 * Holds the chosen Station
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
     * The Tab Pane that holds multiple Series. Can have Multiple Charts each in a new Tab new Tab via Menu -> Loads from FXML
     */
    @FXML
    private TabPane tabPaneCharts;
    
    /**Progress Bar of Reading in of Data-Series */
    @FXML
    private ProgressBar progress;
    
    /**Status of reading in */
    @FXML
    private Label labelProgress;
    
    /**ListView of read-in Series */
    @FXML
    private ListView<Series<String,Double>> listViewSeries;
    
	
    private final ObjectProperty<ObservableList<StationId>> stationIds;
	private final ObjectProperty<ObservableList<Area>> areas;
	
	private final ObjectProperty<LocalDate> startDate, endDate;
	
	private final ObjectProperty<Area> selectedArea;
	private final ObjectProperty<StationId> selectedStationId;	
	/**12=true 0=false */
	private final BooleanProperty time;
	
	private final ObjectProperty<ObservableList<PlotCommand>> plottableValues;
	private final ObjectProperty<PlotCommand> valueToPlot;
	
	private final ObjectProperty<LevelType> levelToPlot;
	private final ObjectProperty<ObservableList<LevelType>> plottableLevels;
	
	private final ObjectProperty<Color> pickedColour;
	
	//private final ObjectProperty<ObservableList<Series<String, Double>>> chartSerien;
    
	public ChartGUIController() {
		
		stationIds			= new SimpleObjectProperty<>(this, "stations",    FXCollections.observableArrayList());
		areas				= new SimpleObjectProperty<>(this, "areas",       FXCollections.observableArrayList());
		startDate			= new SimpleObjectProperty<>(this, "startDate",   LocalDate.now());
		endDate				= new SimpleObjectProperty<>(this, "endDate",     LocalDate.now());
		
		selectedArea		= new SimpleObjectProperty<>(this, "selectedArea");
		selectedStationId	= new SimpleObjectProperty<>(this, "selectedStation");
		
		valueToPlot			= new SimpleObjectProperty<>(this, "selectedPlotCommand");
		//OBSOLET
		plottableValues		= new SimpleObjectProperty<>(this, "plotCommand", FXCollections.unmodifiableObservableList(FXCollections.observableArrayList()));
		
		levelToPlot			= new SimpleObjectProperty<>(this, "selectedLevel");
		//OBSOLET
		plottableLevels		= new SimpleObjectProperty<>(this, "levelsSelection",	FXCollections.observableArrayList());
		
		time				= new SimpleBooleanProperty (this, "time", false);
		pickedColour		= new SimpleObjectProperty<>(this, "picked Colour");
		
		//chartSerien			= new SimpleObjectProperty<>(this, "serien");
		
	}
	
	/**
	 * Autocalled Method - initialized by FXMLLoader
	 */    
	@FXML
	public void initialize()
	{
		//TODO Bindings CellFactorys
		//TODO Tasks			
			
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
				
		buttonAddSeries.disableProperty().bind(Bindings.not(Bindings.createBooleanBinding(this::checkInputValidity,
				choiceStation.getSelectionModel().selectedItemProperty(), 
				choiceRegion.getSelectionModel().selectedItemProperty(),
				pickerStartDate.valueProperty(),
				pickerEndDate.valueProperty(),
				choiceLevel.getSelectionModel().selectedItemProperty(),
				choiceValue.getSelectionModel().selectedItemProperty(),
				colorPickerSeriesColor.valueProperty())));
		
		 tabPaneCharts.getTabs().add(createTab());	
		 tabPaneCharts.requestFocus();
		 
		 tabPaneCharts.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> System.out.println("changed"));
		
		// listViewSeries.itemsProperty().bind(null);
		 
		 //TODO css für GUI
		 
		initializeDataPresentation();		
	}
			
	/**
	 * Initialises all GUI-Elements with the appropriate data Factorys
	 */
	private void initializeDataPresentation()
	{
		choiceRegion.setCellFactory(value -> new ListCell<>() {
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
		
		choiceLevel.setCellFactory(value -> new ListCell<>() {
			@Override
			protected void updateItem(LevelType item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.toString());
				}
			};
		});
		
		choiceLevel.setButtonCell(new ListCell<>() {
			@Override
			protected void updateItem(LevelType item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.toString());
				}
			};
		});
		
		
		choiceValue.setCellFactory(value -> new ListCell<>() {
			@Override
			protected void updateItem(PlotCommand item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.getDisplayName());
				}
			};
		});
		
		choiceValue.setButtonCell(new ListCell<>() {
			@Override
			protected void updateItem(PlotCommand item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.getDisplayName());
				}
			};
		});
	}
	
	/**
	 * Sets the Dataprovider of the Program
	 * @param provider The Dataprovider from where to get Data
	 */
	public void setDataProvider(DataProvider provider)
	{		
		this.provider = provider;	
		setAreas(provider.getAreas());
		setPlottableValues(getAvailableSoundingValues());
		setPlottableLevels(getAvailablePressureLevels());
	}
	
	@FXML
	protected void buttonActionAddSeries(ActionEvent event) 
	{		
		
		System.out.println("Add Series to active Chart");
		
		if (tabPaneCharts.getSelectionModel().getSelectedItem() == null)
			tabPaneCharts.getTabs().add(createTab());
		
		ChartTabController ctc;
		ctc = (ChartTabController)tabPaneCharts.getSelectionModel().getSelectedItem().getUserData();	
				
		Task<Series<Number,Double>> task = new Task<Series<Number,Double>>() {
			private Series<Number, Double> series = new Series<>();
			AtomicInteger current = new AtomicInteger(0);
			
	        @Override 
	        protected Series<Number, Double> call() throws Exception {
	           	updateMessage("Running...");
	        	long max = getStartDate().datesUntil(getEndDate().plusDays(1)).count();
	     		series.setName(getSelectedStationId().getStationName());
	     		
	     		series.setData(
	     				getStartDate().datesUntil(getEndDate().plusDays(1))//.parallel()
		     			.map(date ->
		     				new XYChart.Data<Number,Double>(date.toEpochDay(),
		     						getValueToPlot().execute(
		     								provider.getReading(
		     										getSelectedStationId(),
		     										LocalDateTime.of(date, getTime() ? LocalTime.of(12, 0) : LocalTime.of(0, 0)), 
		     										getLevelToPlot())		     						
		     						.orElse(getDummy(LevelType.CUSTOM)))))
		     			.takeWhile(data -> !isCancelled())
		     			.peek(data -> updateProgress(current.incrementAndGet(), max))
		     			.collect(Collectors.toCollection(FXCollections::observableArrayList))
	     		);
	     		return series;
	         }
	         
	         @Override 
	         protected void succeeded() {
	         	super.succeeded();
	         	updateMessage("Success!");
	         	ctc.addSeries(series);
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
	 * @param event The thrown ActionEvent from the GUI
	 */
	@FXML
	protected void choiceActionRegion(ActionEvent event)
	{
		setStationIds(provider.getStationsByArea(((ComboBox<Area>)event.getSource()).getValue()));
	}
	
	/**
	 * Adds a new Tab with a new Chart to the TabPane tabPaneCharts
	 * @param event The thrown ActionEvent from the GUI
	 */
	@FXML
	protected void menuActionNewChart(ActionEvent event) 
	{
		 System.out.println("New Chart");
		 tabPaneCharts.getTabs().add(createTab());
		 
	}
	
	@FXML
	protected void buttonActionCancel(ActionEvent event)
	{
		((Task<Series<String,Double>>)progress.getUserData()).cancel();
	}
	/**
	 * Creates a new Tab from FXML and sets controller
	 * @return
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
	 * Checks wether all conditions of adding a new Series are fullfilled
	 * @return validity of adding a new Series
	 */
	private boolean checkInputValidity()
	{		
		// FIXME check edge cases
		boolean isValid = true;
		
		isValid &= !(choiceRegion.getValue() == null);
		isValid &= !(choiceStation.getValue() == null);
		isValid &= !(choiceLevel.getValue() == null);
		isValid &= !(choiceValue.getValue() == null);
		isValid &= !(pickerStartDate.getValue() == null);
		isValid &= !(pickerEndDate.getValue() == null);
		//isValid &= !(colorPickerSeriesColor.getValue() == null);
		//System.out.println(isValid);
		
		if(isValid)
		{
			isValid &= !(pickerStartDate.getValue().isAfter(pickerEndDate.getValue()));
			isValid &= !(pickerStartDate.getValue().isAfter(LocalDate.now()));
			isValid &= !(pickerEndDate.getValue().isAfter(LocalDate.now()));
		}
		
		/*if (isValid) {				
			isValid &= !(pickerStartDate.getValue().isAfter(LocalDate.now()));
			isValid &= !(pickerEndDate.getValue().isAfter(LocalDate.now()));
			isValid &= !(pickerStartDate.getValue().isAfter(pickerEndDate.getValue()));			
			//isValid &= !(datePickerStart.getValue().isAfter(datePickerEnde.getValue()));
									
			/*isValid &= !(datePickerStart.getValue().isEqual(datePickerEnde.getValue())&&  //true
					(startTimeChoiceBox.getValue().isAfter(LocalTime.now()) || 			//true
					 startTimeChoiceBox.getValue().isAfter(endTimeChoiceBox.getValue()) ||
					 endTimeChoiceBox.getValue().isAfter(LocalTime.now()) ||
					 	(startTimeChoiceBox.getValue().isAfter(LocalTime.now())) &&
					 	 endTimeChoiceBox.getValue().isAfter(LocalTime.now())));
			
		}*/
		return isValid;
	}
	
	/**
	 * 
	 * @return A List of available Values
	 */
	public List<PlotCommand> getAvailableSoundingValues()
	{
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
	 * 
	 * @return A List of available Pressure Levels to Plot
	 */
	public List<LevelType> getAvailablePressureLevels()
	{
		return List.of(LevelType.values());
	}
	

	
	private Reading getDummy(LevelType type)
	{
		return new Reading(type, 0, 0, 0, 0, 0, 0, 0, 0);
		
		//Wenn nur ein reading fehlt getvorheriges
		
	}
	
	public Property<ObservableList<LevelType>> plottableLevelsProperty()
	{
		return plottableLevels;
	}
	
	public void setPlottableLevels(List<LevelType> dlist)
	{
		if(dlist instanceof ObservableList<?>)
			plottableLevels.set((ObservableList<LevelType>)dlist);
		else
			plottableLevels.set(FXCollections.observableArrayList(dlist));
	}
	
	public ObservableList<LevelType> getPlottableLevels()
	{
		return plottableLevels.get();
	}
	

	public Property<LevelType> levelToPlotProperty()
	{
		return levelToPlot;
	}
	
	public void setLevelToPlot(LevelType doub)
	{
		levelToPlot.set(doub);
	}
	
	public LevelType getLevelToPlot()
	{
		return levelToPlot.get();
	}
	
	public Property<PlotCommand> valueToPlotProperty()
	{
		return valueToPlot;
	}
	
	public void setValueToPlot(PlotCommand plc)
	{
		valueToPlot.set(plc);
	}
	
	public PlotCommand getValueToPlot()
	{
		return valueToPlot.get();
	}
	
	public Property<ObservableList<PlotCommand>> plottableValuesProperty()
	{
		return plottableValues;
	}
	
	public void setPlottableValues(List<PlotCommand> plcom)
	{
		if(plcom instanceof ObservableList<?>)
			plottableValues.set((ObservableList<PlotCommand>)plcom);
		else
			plottableValues.set(FXCollections.observableArrayList(plcom));
	}
	
	public ObservableList<PlotCommand> getPlottableValues()
	{
		return plottableValues.get();
	}
	


	public Property<ObservableList<StationId>> stationIdsProperty() {
		return stationIds;
	}
	
	public ObservableList<StationId> getStationIds()
	{
		return stationIds.get();
	}
	
	public void setStationIds(List<StationId> st)
	{
		if(st instanceof ObservableList<?>)
			stationIds.set((ObservableList<StationId>) st);
		else
			stationIds.set(FXCollections.observableArrayList(st));
	}
	
	public Property<ObservableList<Area>> areaProperty()
	{
		return areas;
	}
	
	public ObservableList<Area> getAreas()
	{
		return areas.get();
	}
	
	public void setAreas(List<Area> area)
	{
		if(area instanceof ObservableList<?>)
			areas.set((ObservableList<Area>) area);
		else
			areas.set(FXCollections.observableArrayList(area));
	}
	
	public Property<LocalDate> startDateProperty()
	{
		return startDate;
	}
	public Property<LocalDate> endDateProperty()
	{
		return endDate;
	}
	
	
	public LocalDate getStartDate()
	{
		return startDate.get();
	}
	
	public LocalDate getEndDate()
	{
		return endDate.get();
	}	
	
	
	
	public void setStartDate(LocalDate date)
	{
		startDate.set(date);
	}
	
	public void setEndDate(LocalDate date)
	{
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
	
	public Area getSelectedArea()
	{
		return selectedArea.get();
	}
	
	public StationId getSelectedStationId()
	{
		return selectedStationId.get();
	}
	
	public void setSelectedStationId(StationId station)
	{
		selectedStationId.set(station);
	}
	
	public void setSelectedArea (Area area)
	{
		selectedArea.set(area);
	}	
	
	public Property<Boolean> timeProperty()
	{
		return time;
	}
	
	public boolean getTime()
	{
		return time.get();
	}
	public void setTime(boolean arg)
	{
		time.set(arg);
	}
	
	public Property<Color> pickedColourProperty()
	{
		return pickedColour;
	}
	
	public Color getPickedColour()
	{
		return pickedColour.get();
	}
	public void setPickedColour(Color arg)
	{
		pickedColour.set(arg);
	}
	

}
