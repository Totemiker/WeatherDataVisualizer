package workspace;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import control.Data_Provider;
import model.Area;
import model.Sounding;

/**
 * Kommunikation mit Dataprovider
 * Manipuliert das Worksheet
 * @author Tobias
 *
 */
public class WorksheetController 
{
	private Data_Provider provider;
	private Worksheet sheet;
	
	public WorksheetController(Worksheet sheet) 
	{
		this.sheet = sheet;
		
	}
	
	public void switchRegionTo(Area value)
	{
		sheet.setStations(provider.getStationsByArea(value));		
	}

	/**
	 * @return the provider
	 */
	public Data_Provider getProvider() {
		return provider;
	}

	/**
	 * @param provider the provider to set
	 */
	public void setProvider(Data_Provider provider) {
		this.provider = provider;
		sheet.setAreas(provider.getAreas());
	}

	/**
	 * @return the sheet
	 */
	public Worksheet getSheet() {
		return sheet;
	}

	/**
	 * @param sheet the sheet to set
	 */
	public void setSheet(Worksheet sheet) {
		this.sheet = sheet;
	}
	
	
	/**
	 * Wird aufgerufen wenn in Der GUI der Button "Clear & Select" gedrückt wird
	 * 
	 */
	public void clearAndSelect() 
	{
			System.out.println("Clear and Select Worksheet Controller");
			
			sheet.getSelectedSoundings().clear();
			
			sheet.setSelectedSoundingsProperty(provider.getSoundingsByStation(
					sheet.getSelectedStation(), 
					LocalDateTime.of(sheet.getStartDate(), sheet.getStartTime()), 
					LocalDateTime.of(sheet.getEndDate(), sheet.getEndTime()), 
					sheet.getSelectedArea()));			
	}
	/**
	 * Wird aufgerufen bei "Add to Selection
	 */
	public void addToSelection()
	{
		Set<Sounding> temp = new HashSet<>();
		temp.addAll(sheet.getSelectedSoundings());
		temp.addAll(provider.getSoundingsByStation(
				sheet.getSelectedStation(),
				LocalDateTime.of(sheet.getStartDate(), sheet.getStartTime()),
				LocalDateTime.of(sheet.getEndDate(), sheet.getEndTime()),
				sheet.getSelectedArea()));
		sheet.getSelectedSoundings().clear();
		sheet.setSelectedSoundingsProperty(temp.stream().collect(Collectors.toList()));
		
	}

}
