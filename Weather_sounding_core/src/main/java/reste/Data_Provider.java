package reste;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.common.collect.HashBiMap;

import model.Area;

/**
 * @author Tobias Teumert
 */

public class Data_Provider 
{		
	private static final List<Area> areas = new ArrayList<>(); 
	//private static final BiMap<Area,?> stationen = HashBiMap.create();
	
	public Data_Provider() 
	{
		areas.add(new Area("eur", "europa"));
		areas.add(new Area("fr", "France"));
		areas.add(new Area("qc", "Quebec"));
		areas.add(new Area("uk", "UK"));
		areas.add(new Area("ca", "Canada"));
		areas.add(new Area("us", "USA"));
		
		//stationen.putAll(map);
	}
	
	/**
	 * Generates a List of Stations from a given URL. URL has to be correct. 
	 * @param URL the representing URL-String which will be analysed as doc
	 * @return A Bi-Directional HashMap with stationID and Station Name
	 */
	public HashBiMap<Integer,String> getStationListFromDoc(String URL)
	{
		Document doc;
		doc = null;
		try {
			doc = Jsoup.connect(URL).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		Elements e = doc.select("select[name=\"stn\"]").select("option");				
		//System.out.println(e);				
		//HashBiMap<Integer, String> test = 
		return  e.stream()
				 .collect(
						Collectors.toMap(
								element -> Integer.parseInt(element.attributes().get("value")), // map the key 
								element -> element.text() ,  // map the value
								(exists, duplicate) -> exists, // what happens to duplicate values?
								HashBiMap::create)); // which map type to instantiate		
		//return test;
	}
	
	/**
	 * 
	 */
	public String getDataFromStationInText(int stationID,LocalDateTime time, String area)
	{		
		String urlBuilder ="http://meteocentre.com/radiosonde/get_sounding.php?lang=en&show=0&hist=0&area="
							+area+"&stn="
							+stationID+"&type=txt&yyyy="
							+time.getYear()+"&mm="
							+time.getMonthValue()+"&dd="
							+time.getDayOfMonth()+"&run="
							+time.getHour();
		Document doc;
		//System.out.println(urlBuilder);
		doc = null;
		
		try {
			doc = Jsoup.connect(urlBuilder).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return doc.select("pre").first().text();
	}	
		
	public List<String> getAreaByCode()
	{
		return areas.stream().map(arg -> arg.getAreaCode()).collect(Collectors.toList());
				
	}
	
	public List<String> getAreaByName()
	{
		return areas.stream().map(arg -> arg.getAreaName()).collect(Collectors.toList());
	}
	
	public List<Area> getAreas()
	{
		return areas;
	}
	
	
	
}
