package model;

import java.time.LocalDateTime;
import java.util.List;

public class Sounding 
{	
	private List<LevelData> leveldata;	
	private LocalDateTime dateAndTime;	
	private Station station;
	
	public Sounding(Station station, LocalDateTime dateAndTime, List<LevelData> leveldata)
	{		
		this.setStation(station);
		this.setDateAndTime(dateAndTime);
		this.setLeveldata(leveldata);
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
	 * @return the station
	 */
	public Station getStation() {
		return station;
	}

	/**
	 * @param station the station to set
	 */
	public void setStation(Station station) {
		this.station = station;
	}

	public List<LevelData> getLeveldata() {
		return leveldata;
	}

	public void setLeveldata(List<LevelData> leveldata) {
		this.leveldata = leveldata;
	}	
}
