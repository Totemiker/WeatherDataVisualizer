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
	 * @param shortArea the shortArea to set
	 */
	public void setAreaCode(String shortArea) {
		this.areaCode = shortArea;
	}

	/**
	 * @return the longArea
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * @param longArea the longArea to set
	 */
	public void setAreaName(String longArea) {
		this.areaName = longArea;
	}
	
}
