package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Klasse zum Speichern eines Soundings
 * @author Tobias
 *
 */
public class Sounding 
{	
	private StationId stationId;
	private Station station;
	private List<Reading> readings = new ArrayList<>();
	private LocalDateTime dateAndTime;		
	
	
	public Sounding(LocalDateTime dateAndTime, List<Reading> readings, Station station, StationId id)
	{		
		this.setDateAndTime(dateAndTime);
		this.setReadings(readings);		
		this.setStation(station);
		this.setStationId(id);
	}
	/**
	 * Leerer Dummy Constructor
	 */
	public Sounding() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the dateAndTime
	 */
	public LocalDateTime getDateAndTime() {
		return dateAndTime;
	}

	/**
	 * @param dateAndTime the dateAndTime to set
	 */
	public void setDateAndTime(LocalDateTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
	
	/**
	 * Getter for List of available Readings
	 * @return List of available Readings
	 */
	public List<Reading> getReadings() {
		return readings;
	}
	/**
	 * Set the available Readings
	 * Invariant: this.readings is never NULL(!)
	 * @param readings List of available Readings
	 */
	public void setReadings(List<Reading> readings) {
		if (readings == null) {
			this.readings.clear();
		} else this.readings = readings;
	}
	
	/**
	 * Getter for station
	 * @return	The Station of the Sounding
	 */
	public Station getStation() {
		return station;
	}
	
	/**
	 * Sets the station of the Sounding
	 * @param station The station to set
	 */
	public void setStation(Station station) {
		this.station = station;
	}

	public StationId getStationId() {
		return stationId;
	}
	public void setStationId(StationId stationId) {
		this.stationId = stationId;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
