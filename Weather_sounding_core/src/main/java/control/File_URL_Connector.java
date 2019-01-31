package control;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.HashBiMap;

import model.Sounding;


/**
 * HTML Binding to internet-Url
 * Getting Data from connected URL
 * 
 * Data File Structure
 * /bin/data/"region"/"station id"/stationId_yyyy_mm_dd_hh.txt
 * /bin/data/EUR/1014/1014_2018_12_26_12.txt
 * 
 * @author Tobias Teumert
 * 
 */
public class File_URL_Connector 
{	
	public void loadURL()
	{
		Document doc;
		doc = null;
		try {
			doc = Jsoup.connect(
					//"http://meteocentre.com/radiosonde/get_sounding.php?lang=en&show=0&hist=0&area=eur&stn=10410&type=txt&yyyy=2019&mm=01&dd=25&run=12")
					"http://meteocentre.com/radiosonde/get_sounding.php?area=eur")//hardcoded einstiegstring
					.get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	
	
	
	
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
		HashBiMap<Integer, String> test = 
				e.stream()
				 .collect(
						Collectors.toMap(
								element -> Integer.parseInt(element.attributes().get("value")), // map the key 
								element -> element.text() ,  // map the value
								(exists, duplicate) -> exists, // what happens to duplicate values?
								HashBiMap::create)); // which map type to instantiate		
		return test;
	}
	
	/**
	 * 
	 */
	public String getDataFromStationInText(int stationID,LocalDateTime time)
	{		
		String urlBuilder ="http://meteocentre.com/radiosonde/get_sounding.php?lang=en&show=0&hist=0&area=eur&stn="
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
	
	public boolean writeDataToFile(Sounding sound)
	{
		//String file = "C:\\Users\\Tobias\\Documents\\WeatherSoundingData";
		
		
		
		//File dataStorage = new File(pathname)
		return false;
	}
	
	public void loadIni()
	{
		//check ob ini da ist, wenn ja laden, wenn nein erzeugen (mit Standardparametern)
		
	}
	
}
