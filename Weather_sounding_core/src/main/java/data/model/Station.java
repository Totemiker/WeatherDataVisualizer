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
package data.model;

/**
 * Representation of a physical station which generates the sounding data by atmospheric probe starts (0 UTC and 12 UTC)<p>
 * Contains longitude and latitude in decimals rather then degrees
 * @author Tobias
 *
 */
public class Station {

	/** Unique Station ID */
	private StationId stationId;
	/** Name der Station */
	private String stationName;	
	/** The longitude of the station in decimals*/
	private double longitude;
	/** The latitude of the station in decimals*/
	private double latitude;	
	/** The elevation of station in meters */
	private int elevation;	
	/**The ICAO Code */
	private String icao;
	/**The Country Code */
	private String country;//FIXME REGEX NOCH PUTT geht noch nicht
	
	/*public Station()
	{
		
	}*/
	/**
	 * Constructor for Station
	 * @param stationID 	StationID Object
	 * @param stationName 	Name of Station
	 * @param longi 		Longitude in Decimals
	 * @param lati			Latitude in Decimals
	 * @param elevation		elevation in meters
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
	 * Getter for station name
	 * @return the stationName
	 */
	public String getStationName() {
		return stationName;
	}

	/**
	 * Setter for Station Name
	 * @param stationName the stationName to set
	 */
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	/**
	 * Getter for Station ID
	 * @return the stationID
	 */
	public StationId getStationID() {
		return stationId;
	}

	/**
	 * Setter for Station ID
	 * @param stationID the stationID to set
	 */
	public void setStationID(StationId stationID) {
		this.stationId = stationID;
	}
	
	/**
	 * getter for Longitude
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Setter for longitude
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Getter for Latitude
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Setter for Latitude
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Getter for the elevation
	 * @return the elevation
	 */
	public int getElevation() {
		return elevation;
	}

	/**
	 * Setter for the elevation of the station
	 * @param elevation the elevation to set
	 */
	public void setElevation(int elevation) {
		this.elevation = elevation;
	}

	/**
	 * Getter for ICAO Code
	 * @return the stationCode
	 */
	public String getIcao() {
		return icao;
	}

	/**
	 * Setter for ICAO Code
	 * @param stationCode the ICAO Code to set
	 */
	public void setIcao(String stationCode) {
		this.icao = stationCode;
	}
	/**
	 * Getter for Country Code
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Setter for Country Code
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
