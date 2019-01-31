package model;

import java.time.LocalDateTime;
import java.util.List;

public class Sounding 
{	
	private List<LevelData> leveldata;	
	private LocalDateTime dateAndTime;	
	private double longitude,latitude;	
	private int elevation;
	private String stationCode, country;
		
	public Sounding(LocalDateTime dateAndTime, List<LevelData> leveldata, double longitude, double lat, int elevation,
			String stationCode, String country)
	{		
		this.setDateAndTime(dateAndTime);
		this.setLeveldata(leveldata);
		this.longitude = longitude;
		this.latitude = lat;
		this.elevation = elevation;
		this.stationCode = stationCode;
		this.country = country;
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

	public List<LevelData> getLeveldata() {
		return leveldata;
	}

	public void setLeveldata(List<LevelData> leveldata) {
		this.leveldata = leveldata;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the elevation
	 */
	public int getElevation() {
		return elevation;
	}

	/**
	 * @param elevation the elevation to set
	 */
	public void setElevation(int elevation) {
		this.elevation = elevation;
	}

	/**
	 * @return the stationCode
	 */
	public String getStationCode() {
		return stationCode;
	}

	/**
	 * @param stationCode the stationCode to set
	 */
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
}
