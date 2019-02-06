package model;

public class Area 
{
	private String areaCode,areaName;
	/**
	 * 
	 * @param s1 Abkürzung
	 * @param s2 Auschgeschriebener Area Name
	 */
	public Area(String s1,String s2)
	{
		areaCode = s1;
		areaName = s2;
	}

	/**
	 * @return the shortArea
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * @return the longArea
	 */
	public String getAreaName() {
		return areaName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((areaCode == null) ? 0 : areaCode.hashCode());
		result = prime * result + ((areaName == null) ? 0 : areaName.hashCode());
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
		Area other = (Area) obj;
		if (areaCode == null) {
			if (other.areaCode != null)
				return false;
		} else if (!areaCode.equals(other.areaCode))
			return false;
		if (areaName == null) {
			if (other.areaName != null)
				return false;
		} else if (!areaName.equals(other.areaName))
			return false;
		return true;
	}
	
}
