package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import control.DataProvider;
import control.WebDataProvider;
import model.Reading.LevelType;

public class WeatherDataParser {
	
	@FunctionalInterface
	public static interface StationBuilder {
		public Station buildStation(StationId stationID, String stationName, double longi,double lati, int elevation, String icao);
	}
	
	StationBuilder stationSupplier = Station::new;
	
	public WeatherDataParser(StationBuilder builder) {
		this.stationSupplier = builder;
	}

	/**
	 * validates a given Input String to valid Data
	 * @param toParse	The Input Data
	 * @return	The Result. It contains the validated Data (Sounding) and in case of no valid Data the Type of Exception and an boolean (valid)
	 */
	public WeatherDataParserValidationResult validate(String toParse, StationId statID) {
		WeatherDataParserValidationResult result = new WeatherDataParserValidationResult();
		Sounding data = new Sounding();
		try {
			
			StringReader reader = new StringReader(toParse);
			BufferedReader buf = new BufferedReader(reader);

			List<String> tokens = buf.lines().collect(Collectors.toList());
			
			if (tokens.stream()
					.anyMatch(element -> element.contains("No observations found for selection conditions"))){
				data.setStationId(statID);
				data.setStation(stationSupplier.buildStation(statID, "", 0, 0, 0, ""));
				throw new Exception("No observations found for selection conditions");
			}
									
			splitFirstHeaderLine(tokens.get(2), data);
			data.setStation(splitSecondHeaderLine(tokens.get(5),statID));
			data.setStationId(statID);
			
			List<Reading> levelDatas;

			if (tokens.size() < 5) {
				data.setStationId(statID);
				data.setStation(stationSupplier.buildStation(statID, "", 0, 0, 0, ""));
				throw new Exception("Zu wenig Level im Datensatz");
				
			} else {
				
				String dpat = "([-+]?[0-9]*\\.?[0-9]+||-{2,})";
				Pattern pattern = Pattern.compile("\\s*([A-Z]+[0-9]?)\\s*"+dpat+"\\s*"+dpat+"\\s*"+dpat+"\\s*"+dpat+"\\s*"+dpat+"\\s*"+dpat+"\\s*"+dpat+"\\s*"+dpat);
				//Matcher match;
				
				List<String> levelDataString = tokens.stream()
					.filter(arg -> pattern.matcher(arg).matches())    //Alle Zeilen die von der regex captured werden
					.map(arg -> arg.trim())
					.map(arg -> arg.contains("---") ? arg.replace("-----", "0") : arg)
					.filter(arg -> arg.length() != 0).collect(Collectors.toList());
				
				levelDatas = levelDataString.stream().map(arg -> createReading(arg.split("\\s+")))
						.collect(Collectors.toList());
				
				/*
				List<String> levelDataString = Stream.iterate(beginOfTable, i -> i + skip).limit(limit).map(tokens::get)
						.map(arg -> arg.trim()).map(arg -> arg.contains("---") ? arg.replace("-----", "0") : arg)
						.filter(arg -> arg.length() != 0).collect(Collectors.toList());
				
				levelDatas = levelDataString.stream().map(arg -> createReading(arg.split("\\s+")))
						.collect(Collectors.toList());*/
				data.setReadings(levelDatas);
			}
			
			result.setData(data);
		} catch (Exception e) {
			//e.printStackTrace();
			result.setValid(false);
			result.setException(e);
			result.setData(data);
			return result;

		}
		return result;
	}
	
	/**
	 * Creates the correct Leveltype in the Reading
	 * @param split the Array to parse as Reading
	 * @return the parsed Reading
	 */
	private static Reading createReading(String[] split) 
	{
		LevelType type = null;
		if(split[0].equals("MAND")) {
			split[0] = "MAND_"+((int)Double.parseDouble(split[1]));
			type = LevelType.valueOf(split[0]);
		}
		else if(split[0].equals("TRO1") || split[0].equals("TRO2") || split[0].equals("SFC") || split[0].equals("MAXW"))
			type = LevelType.valueOf(split[0]);
		else
			type = LevelType.CUSTOM;
		
		Reading data = new Reading(split, type); 
		return data;
	}
	
	/**
	 * Splits the first Stationdata line of a sounding
	 * @param line
	 * @param data
	 * @return
	 */
	private static Sounding splitFirstHeaderLine(String line, Sounding data)
	{		
		Pattern linePattern = Pattern.compile("SOUNDING # [0-9]+\\s+IDN=\\s*(?<id>[0-9]+)\\s+DAY=(?<year>[0-9]{4})(?<day>[0-9]{3})\\s+TIME=\\s*(?<time>[0-9]+)\\s+VALID LEVELS=\\s*(?<lvl>[0-9]+)");
		Matcher lineMatcher = linePattern.matcher(line);
		if (lineMatcher.matches()) {
			data.setDateAndTime(
					LocalDateTime.of(
							LocalDate.ofYearDay(
					Integer.parseInt(lineMatcher.group("year")),
					Integer.parseInt(lineMatcher.group("day"))),
							LocalTime.of(Integer.parseInt(lineMatcher.group("time"))/10000, 0)));
		}
		return data;
	}
	
	private Station splitSecondHeaderLine(String line, StationId id)
	{
		int elevation=0;
		double longitude=0,latitude=0;
		String icao="",stationName = "";
		Pattern linePattern = Pattern.compile("\\s*[0-9]+\\s*(?<icao>[A-Z]{4})?\\s*(?<station>[A-Za-z-.]*\\s?/?\\s?[A-Za-z.]*\\s?[a-zA-Z.]*)\\s*(?<state>[A-Z]{2})?\\s*(?<country>[A-Z]{2})?\\s*(?<latdeg>[0-9]{2}):(?<latmin>[0-9]{2})(?<lat>[A-Z])\\s*(?<longdeg>[0-9]{3}):(?<longMin>[0-9]{2})(?<long>[A-Z])\\s*(?<elev>[0-9]*)");
		Matcher lineMatcher = linePattern.matcher(line);
		if (lineMatcher.matches()) {
			elevation = Integer.parseInt(lineMatcher.group("elev"));
			longitude = Double.parseDouble(lineMatcher.group("longdeg"))+Double.parseDouble(lineMatcher.group("longMin"))/60;
			if(lineMatcher.group("long").equals("S"))
				longitude = -longitude;
			
			latitude = Double.parseDouble(lineMatcher.group("latdeg"))+Double.parseDouble(lineMatcher.group("latmin"))/60;
			if(lineMatcher.group("lat").equals("W"))
				latitude = -latitude;
			
			icao = lineMatcher.group("icao");
			stationName = lineMatcher.group("station");
			
		}
		return stationSupplier.buildStation(id, stationName, longitude, latitude, elevation, icao);
		//return null;
	}	
	
	public static void main(String[] args) {
		
		Document doc = null;
		try {
			doc = Jsoup.connect("http://meteocentre.com/radiosonde/get_sounding.php?stn=10410&type=txt&yyyy=2018&mm=10&dd=28&run=12&hist=1&show=0&lang=en&area=eur").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DataProvider provider = new WebDataProvider(new Properties());
		WeatherDataParser parser = new WeatherDataParser(provider::buildStation);
		parser.validate(doc.select("pre").first().text(),new StationId(10410, "ALPHA", new Area("eur", "Europa")));
		
		//WeatherDataParser parser = new WeatherDataParser(Station::new);
		WeatherDataParserValidationResult result = parser.validate(doc.select("pre").first().text(),new StationId(10410, "ALPHA",new Area("eur", "Europa")));
		System.out.println(result.isValid());
	}
	
}

