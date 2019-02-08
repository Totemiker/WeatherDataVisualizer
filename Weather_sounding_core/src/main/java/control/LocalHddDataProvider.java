package control;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import model.Area;
import model.Sounding;
import model.Station;
import model.StationId;
import model.WeatherDataParser;
import model.WeatherDataParserValidationResult;

public class LocalHddDataProvider extends ChainedDataProvider{

	WeatherDataParser parser;
	
	/**
	 * 
	 * File Structure property\\$region_Code\\$Station_Code\\$LocalDateTime.txt
	 * 
	 * @param provider
	 * @param prop
	 */
	public LocalHddDataProvider(DataProvider provider, Properties prop) {
		this.properties = prop;
		this.upstream = provider;
		parser = new WeatherDataParser(this::buildStation);
	}
	
	@Override
	public List<StationId> getStationsByArea(Area area) {
		return upstream.getStationsByArea(area);
		//return null;
	}

	@Override
	public Sounding getSounding(StationId station, LocalDateTime time) {
		//Load Sounding with parser from HDD or push it one up to webprovider
		//System.out.println("Im HDD Provider hier muss was passieren");
		String datadir = ""+properties.get("data_dir");
		//System.out.println("DataDir = "+datadir);
		
		//System.out.println("Station ="+station);
		
		Path regionP = Path.of(datadir)
				.resolve(""+station.getArea().getAreaCode())
				.resolve(""+station.getStationID())
				.resolve(""+time.toLocalDate().format(DateTimeFormatter.ISO_DATE)+"T"+time.getHour()+".txt");
		
		if(Files.exists(regionP))//Region existiert
		{
			System.out.println("File Exists");
			
			try (BufferedReader br =
	                   new BufferedReader(new FileReader(regionP.toFile(),Charset.forName("UTF-8")))){
				
				List<String> tokens = br.lines().collect(Collectors.toList());
				//System.out.println("Tokens aus dem reader in hdd read");
				//tokens.forEach(System.out::println);
				WeatherDataParserValidationResult result = parser.validate(tokens.stream().collect(Collectors.joining("\r\n")), station);
				
				if (result.isValid()) {
					System.out.println("Vaild Result from parser");
					// soundingsByStation.put(station, result.getData());
					stations.add(result.getData().getStation());
					return result.getData();
				} else {
					System.out.println("No Valid Result from Parser");
					System.out.println(result.getException());
					// result.getException().printStackTrace();
					result.getData().setDateAndTime(time);
					return result.getData();
				}
							
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}		
			return upstream.getSounding(station, time);
		}
		else
			return upstream.getSounding(station, time);
		
		//return null;
	}

	@Override
	public Station buildStation(StationId stationID, String stationName, double longi, double lati, int elevation,
			String icao) {
		
		Station station = new Station(stationID, stationName, longi, lati, elevation, icao);
		return stations.stream().filter(arg -> arg.equals(station)).findFirst().orElse(station);
		
	}

}
