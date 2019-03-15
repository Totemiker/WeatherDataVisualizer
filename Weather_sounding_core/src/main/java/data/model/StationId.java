package data.model;

/**
 * Data-Class<p>
 * Connects Station ID with a Station name and the Area in which said Station geographically lies.
 * @author Tobias
 *
 */
public class StationId 
{	
	/**
	 * The Name of the Station, mostly village or City in which Station resides 
	 */
	private String stationName;
	/**
	 * the unique Station ID
	 */
	private int stationID;
	/**
	 * The Predefined Area in which a Station resides (Not Sure about Conventions apllied)
	 */
	private Area area;
	
	public StationId(int id, String name, Area area) {
		this.stationName = name;
		this.stationID = id;
		this.area = area;
	}
	
	/**
	 * @return the stationName
	 */
	public String getStationName() {
		return stationName;
	}
	
	/**
	 * @return the stationID
	 */
	public int getStationID() {
		return stationID;
	}	
	/**
	 * 
	 * @return the Area in which the station resides
	 */
	public Area getArea() {
		return area;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result + stationID;
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
		StationId other = (StationId) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (stationID != other.stationID)
			return false;
		if (stationName == null) {
			if (other.stationName != null)
				return false;
		} else if (!stationName.equals(other.stationName))
			return false;
		return true;
	}

	
	
	
}
