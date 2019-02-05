package workspace;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import control.Data_Provider;
import control.PlotCommand;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.paint.Color;
import model.Area;
import model.LevelData;
import model.LevelData.LevelType;
import model.Sounding;
import model.Station;

/**
 * Kommunikation mit Dataprovider Manipuliert das Worksheet
 * 
 * @author Tobias
 *
 */
public class WorksheetController {
	private Data_Provider provider;
	private Worksheet sheet;
	
	
	private final ObjectProperty<ObservableList<Station>> stations;
	private final ObjectProperty<ObservableList<Area>> areas;
	
	private final ObjectProperty<LocalDate> startDate, endDate;
	
	private final ObjectProperty<Area> selectedArea;
	private final ObjectProperty<Station> selectedStation;	
	/**12=true 0=false */
	private final BooleanProperty time;
	
	private final ObjectProperty<ObservableList<PlotCommand>> plottableValues;
	private final ObjectProperty<PlotCommand> valueToPlot;
	
	private final ObjectProperty<LevelType> levelToPlot;
	private final ObjectProperty<ObservableList<LevelType>> plottableLevels;
	
	private final ObjectProperty<Color> pickedColour;
	
	//private final OB

	public WorksheetController(Worksheet sheet) {
		this.sheet = sheet;
		
		stations			= new SimpleObjectProperty<>(this, "stations",    FXCollections.observableArrayList());
		areas				= new SimpleObjectProperty<>(this, "areas",       FXCollections.observableArrayList());
		startDate			= new SimpleObjectProperty<>(this, "startDate",   LocalDate.now());
		endDate				= new SimpleObjectProperty<>(this, "endDate",     LocalDate.now());
		
		selectedArea		= new SimpleObjectProperty<>(this, "selectedArea");
		selectedStation		= new SimpleObjectProperty<>(this, "selectedStation");
		
		valueToPlot			= new SimpleObjectProperty<>(this, "selectedPlotCommand");
		plottableValues		= new SimpleObjectProperty<>(this, "plotCommand", FXCollections.unmodifiableObservableList(FXCollections.observableArrayList()));
		
		levelToPlot			= new SimpleObjectProperty<>(this, "selectedLevel");
		plottableLevels		= new SimpleObjectProperty<>(this, "levelsSelection",	FXCollections.observableArrayList());
		
		time				= new SimpleBooleanProperty (this, "time", false);
		pickedColour		= new SimpleObjectProperty<>(this, "picked Colour");
		
		//provider.get

	}
	
	
	public void switchRegionTo(Area value) {
		setStations(provider.getStationsByArea(value));
	}

	/**
	 * @return the provider
	 */
	public Data_Provider getProvider() {
		return provider;
	}

	/**
	 * @param provider the provider to set
	 */
	public void setProvider(Data_Provider provider) {
		this.provider = provider;
		setAreas(provider.getAreas());
		setPlottableValues(provider.getAvailableSoundingValues());
		setPlottableLevels(provider.getAvailablePressureLevels());
	}

	/**
	 * @return the sheet
	 */
	public Worksheet getSheet() {
		return sheet;
	}

	/**
	 * @param sheet the sheet to set
	 */
	public void setSheet(Worksheet sheet) {
		this.sheet = sheet;
	}	
	
	/**
	 * wird aufgerufen beim Drücken auf "Chart"
	 */
	public void chart() // boolean replace
	{/*
		
		Series<String, Double> series = new XYChart.Series<>();
		series.setName(valueToPlot.get().getDisplayName());	
		series.setData(
		sheet.getSelectedSoundings().stream().filter(sounding -> !sounding.getLeveldata().get(0).getLevel().equals("NULL")).map(sounding -> //(sounding.getLeveldata().get(0).equals("NULL")) 
			new Data<>(
					sounding.getDateAndTime().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)), // date and time from sounding
					sounding.getLeveldata()
						.stream() // filter by selected pressure level
						.filter(data -> levelToPlot.get() == data.getLevel()) 
						.map(valueToPlot.get()::execute) // extract value to plot
						.findFirst().orElseGet(() -> 0d) // only one level selected
			))
			  .collect(Collectors.toCollection(FXCollections::observableArrayList)));
		//series.getChart().
		//sheet.getSelectedSoundings().stream().filter(predicate)
		// (if replace) then clear else no-op
		sheet.dataToChartProperty().getValue().clear();
		sheet.dataToChartProperty().getValue().add(series);*/
				
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
	


	public Property<ObservableList<Station>> stationsProperty() {
		return stations;
	}
	
	public ObservableList<Station> getStations()
	{
		return stations.get();
	}
	
	public void setStations(List<Station> st)
	{
		if(st instanceof ObservableList<?>)
			stations.set((ObservableList<Station>) st);
		else
			stations.set(FXCollections.observableArrayList(st));
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
	public Property<Station> selectedStationProperty() {
		return selectedStation;
	}
	
	public Area getSelectedArea()
	{
		return selectedArea.get();
	}
	
	public Station getSelectedStation()
	{
		return selectedStation.get();
	}
	
	public void setSelectedStation(Station station)
	{
		selectedStation.set(station);
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