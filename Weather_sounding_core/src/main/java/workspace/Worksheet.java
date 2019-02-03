package workspace;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Area;
import model.Sounding;
import model.Station;

/**
 * Hält die Arbeitsdaten vor (Viewmodel) 
 * @author Tobias
 *
 */
public class Worksheet {	
	
	private final ObjectProperty<ObservableList<Station>> stationsProperty;
	private final ObjectProperty<ObservableList<Area>> areaProperty;
	private final ObjectProperty<LocalDate> startDateProperty, endDateProperty;
	private final ObjectProperty<ObservableList<LocalTime>> availableTimesProperty;
	private final ObjectProperty<ObservableList<Sounding>> selectedSoundingsProperty;	
	private final ObjectProperty<LocalTime> startTimeProperty, endTimeProperty;
	private final ObjectProperty<Area> selectedAreaProperty;
	private final ObjectProperty<Station> selectedStationProperty;
	
	public Worksheet() {
		stationsProperty 		 = new SimpleObjectProperty<>(this, "stations",    FXCollections.observableArrayList());
		areaProperty     		 = new SimpleObjectProperty<>(this, "areas",       FXCollections.observableArrayList());
		startDateProperty		 = new SimpleObjectProperty<>(this, "startDate",   LocalDate.now());
		endDateProperty  		 = new SimpleObjectProperty<>(this, "endDate",     LocalDate.now());
		availableTimesProperty	 = new SimpleObjectProperty<>(this, "availableTimes",   FXCollections.unmodifiableObservableList(FXCollections.observableArrayList()));
		selectedSoundingsProperty= new SimpleObjectProperty<>(this, "selectedSoundings", FXCollections.observableArrayList());
		
		startTimeProperty	 	 = new SimpleObjectProperty<>(this, "startTime");
		endTimeProperty 		 = new SimpleObjectProperty<>(this, "endTime");
		selectedAreaProperty 	 = new SimpleObjectProperty<>(this, "selectedArea");
		selectedStationProperty  = new SimpleObjectProperty<>(this, "selectedStation");
		
						
		setAvailableTimes(Arrays.asList(LocalTime.of(0, 0),LocalTime.of(12, 0)));
	
	}
	
	public Property<ObservableList<Sounding>> selectedSoundingsProperty()
	{
		return selectedSoundingsProperty;
	}
	
	public void setSelectedSoundingsProperty(List<Sounding> sound)
	{
		if(sound instanceof ObservableList<?>)
			selectedSoundingsProperty.set((ObservableList<Sounding>) sound);
		else
			selectedSoundingsProperty.set(FXCollections.observableArrayList(sound));
	}
	
	public ObservableList<Sounding> getSelectedSoundings()
	{
		return selectedSoundingsProperty.get();
	}

	public Property<ObservableList<Station>> stationsProperty() {
		return stationsProperty;
	}
	
	public ObservableList<Station> getStations()
	{
		return stationsProperty.get();
	}
	
	public void setStations(List<Station> stations)
	{
		if(stations instanceof ObservableList<?>)
			stationsProperty.set((ObservableList<Station>) stations);
		else
			stationsProperty.set(FXCollections.observableArrayList(stations));
	}
	
	public Property<ObservableList<Area>> areaProperty()
	{
		return areaProperty;
	}
	
	public ObservableList<Area> getAreas()
	{
		return areaProperty.get();
	}
	
	public void setAreas(List<Area> area)
	{
		if(area instanceof ObservableList<?>)
			areaProperty.set((ObservableList<Area>) area);
		else
			areaProperty.set(FXCollections.observableArrayList(area));
	}
	
	public Property<LocalDate> startDateProperty()
	{
		return startDateProperty;
	}
	public Property<LocalDate> endDateProperty()
	{
		return endDateProperty;
	}
	public Property<ObservableList<LocalTime>> availableTimesProperty()
	{
		return availableTimesProperty;
	}	
	
	public LocalDate getStartDate()
	{
		return startDateProperty.get();
	}
	
	public LocalDate getEndDate()
	{
		return endDateProperty.get();
	}	
	
	public ObservableList<LocalTime> getAvailableTimes()
	{
		return availableTimesProperty.get();
	}	
	
	public void setStartDate(LocalDate date)
	{
		startDateProperty.set(date);
	}
	public void setEndDate(LocalDate date)
	{
		endDateProperty.set(date);
	}
	public void setAvailableTimes(List<LocalTime> times)
	{
		if(times instanceof ObservableList<?>)
			availableTimesProperty.set((ObservableList<LocalTime>) times);
		else
			availableTimesProperty.set(FXCollections.observableArrayList(times));
	}
	
	public void setStartTime(LocalTime time)
	{
		startTimeProperty.set(time);
	}
	
	public void setEndTime(LocalTime time)
	{
		endTimeProperty.set(time);
	}
	
	public Property<LocalTime> startTimeProperty()
	{
		return startTimeProperty;
	}
	
	public Property<LocalTime> endTimeProperty()
	{
		return endTimeProperty;
	}
	
	public LocalTime getStartTime()
	{
		return startTimeProperty.get();
	}
	
	public LocalTime getEndTime()
	{
		return endTimeProperty.get();
	}

	/**
	 * @return the selectedAreaProperty
	 */
	public Property<Area> selectedAreaProperty() {
		return selectedAreaProperty;
	}

	/**
	 * @return the selectedStationProperty
	 */
	public Property<Station> selectedStationProperty() {
		return selectedStationProperty;
	}
	
	public Area getSelectedArea()
	{
		return selectedAreaProperty.get();
	}
	
	public Station getSelectedStation()
	{
		return selectedStationProperty.get();
	}
	
	public void setSelectedStation(Station station)
	{
		selectedStationProperty.set(station);
	}
	
	public void setSelectedArea (Area area)
	{
		selectedAreaProperty.set(area);
	}
}
