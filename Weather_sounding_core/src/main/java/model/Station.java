package model;


public class Station {

	
	private String stationName, stationShort, country;
	
	private double longitude,latitude;	
	
	public Station(String stationName, String stationShort, String country, double longitude, double latitude,
			int stationID, int elevation)
	{
		this.country = country;
		this.stationName = stationName;
		this.stationShort = stationShort;
		this.elevation = elevation;
		this.stationID = stationID;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	/**
	 * @return the stationName
	 */
	public String getStationName() {
		return stationName;
	}

	/**
	 * @param stationName the stationName to set
	 */
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	/**
	 * @return the stationShort
	 */
	public String getStationShort() {
		return stationShort;
	}

	/**
	 * @param stationShort the stationShort to set
	 */
	public void setStationShort(String stationShort) {
		this.stationShort = stationShort;
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
	 * @return the stationID
	 */
	public int getStationID() {
		return stationID;
	}

	/**
	 * @param stationID the stationID to set
	 */
	public void setStationID(int stationID) {
		this.stationID = stationID;
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

	private int stationID, elevation;
	
	
}
