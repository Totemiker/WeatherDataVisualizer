package model;

/**
 * Stationsklasse für die Speicherung der Stationsdaten
 * @author Tobias
 *
 */
public class Station {

	private StationId stationId;
	/** Name der Station */
	private String stationName;	
	/** longitude */
	private double longitude;
	/** */
	private double latitude;	
	/**Elevation of Station */
	private int elevation;	
	/**ICAO Code */
	private String icao;
	/**Country Code */
	private String country;//REGEX NOCH PUTT geht noch nicht
	
	/*public Station()
	{
		
	}*/
	/**
	 * Constructor for Station
	 * @param stationID 	StationID Object
	 * @param stationName 	Name of Station
	 * @param longi 		Longitude in Decimals
	 * @param lati			Latitude in Decimals
	 * @param elevation		elevation in M
	 * @param icao			International ICAO Code if applicable
	 */
	public Station(StationId stationID, String stationName, double longi,double lati, int elevation, String icao)
	{
		this.setStationID(stationID);
		this.setElevation(elevation);
		this.setIcao(icao);
		this.setStationName(stationName);
		this.setLatitude(lati);
		this.setLongitude(longi);
	}
	
	public Station() {
		
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
	public StationId getStationID() {
		return stationId;
	}

	/**
	 * @param stationID the stationID to set
	 */
	public void setStationID(StationId stationID) {
		this.stationId = stationID;
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
	public String getIcao() {
		return icao;
	}

	/**
	 * @param stationCode the ICAO Code to set
	 */
	public void setIcao(String stationCode) {
		this.icao = stationCode;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + elevation;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));		
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((icao == null) ? 0 : icao.hashCode());
		result = prime * result + stationId.hashCode();
		result = prime * result + ((stationName == null) ? 0 : stationName.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Station other = (Station) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (elevation != other.elevation)
			return false;
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
			return false;
		if (icao == null) {
			if (other.icao != null)
				return false;
		} else if (!icao.equals(other.icao))
			return false;
		if (stationName == null) {
			if (other.stationName != null)
				return false;
		} else if (!stationName.equals(other.stationName))
			return false;
		if(stationId == null) {
			if(other.stationId != null)
				return false;
		} else if(!stationId.equals(other.stationId))
			return false;
		return true;
	}	
}
