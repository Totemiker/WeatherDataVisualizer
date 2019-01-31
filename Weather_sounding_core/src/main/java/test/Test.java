package test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Properties;

import com.google.common.collect.BiMap;

import control.File_URL_Connector;
import control.Weather_Data_Parser;
import model.Sounding;

public class Test {

	static String einstieg;
	
	public static void main(String[] args)
	{
		File_URL_Connector dataimporter = new File_URL_Connector();
		Weather_Data_Parser parser = new Weather_Data_Parser();		
		
		einstieg = "http://meteocentre.com/radiosonde/get_sounding.php?area=eur";
		
		//dataimporter.getStationListFromDoc(einstieg).forEach((arg1,arg2) -> System.out.println("Key: "+arg1+" Value: "+arg2));
		BiMap<Integer, String> mapOfStations = dataimporter.getStationListFromDoc(einstieg);
		
		//Bimdataimporter.getStationListFromDoc(einstieg);
		
		//Map<Integer,String> id_name =  dataimporter.getStationListFromDoc(einstieg);
		//Map<String,Integer> name_id = id_name.
		
		int stationID = mapOfStations.inverse().get("Milano (IT)");
		
		LocalDateTime ldt = LocalDateTime.of(2019, 01, 28, 12, 00);
		
		String s = dataimporter.getDataFromStationInText(stationID, ldt);
		String region = "EUR";
		
		//System.out.println("String in main: "+s);
		
		
		Sounding sound = parser.generateSounding(s,region);
		
		//System.out.println(sound.getStation());
		
		//System.out.println("Sound " + sound.toString());
		
		//String s = dataimporter.readURL2();
		//Weather_Data_Parser parser = new Weather_Data_Parser();
		//parser.generateSounding(s);	
		
		/*Properties properties = new Properties();
		properties.load(null);
		
		properties.get("last_open_file");
		
		properties.setProperty("last_open_file", value);
		
		properties.st*/
		
		// rumgehampel mit Path
		
		
		
		
		
		Path installationsort = Paths.get(System.getProperty("user.dir"));
		System.out.println(installationsort);
		
		Path iniFile = Paths.get(System.getProperty("user.dir")+"\\ini.cfg");
		
		//Path dataFile = 
		
		System.out.println(Files.exists(iniFile));
		if(!Files.exists(iniFile))
		{//Create File
			try {
				Files.createFile(iniFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else
		{
			//load File
		}
		

	}

}
