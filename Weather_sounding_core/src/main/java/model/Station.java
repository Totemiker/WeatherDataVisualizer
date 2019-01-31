package model;

/**
 * Stationsklasse für die Speicherung der zum Namen gehörenden ID
 * @author Tobias
 *
 */
public class Station {

	/**
	 * ID der Station
	 */
	private int stationID;
	/**
	 * Name der Station aus Dropdown-Menu
	 */
	private String stationName;	
	
	public Station(int stationID, String stationName)
	{
		this.stationID = stationID;
		this.stationName = stationName;
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
	
}
