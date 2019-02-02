package model;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeatherDataParser {

	public static WeatherDataParserValidationResult validate(String toParse) {
		WeatherDataParserValidationResult result = new WeatherDataParserValidationResult();

		try {
			Sounding data = new Sounding();
			StringReader reader = new StringReader(toParse);
			BufferedReader buf = new BufferedReader(reader);

			List<String> tokens = buf.lines().collect(Collectors.toList());
			
			if (tokens.stream()
					.anyMatch(element -> element.contains("No observations found for selection conditions"))) {
				throw new Exception("No observations found for selection conditions");
			}
									
			splitFirstHeaderLine(tokens.get(2), data);
			splitSecondHeaderLine(tokens.get(5), data);
			
			int beginOfTable = 11;
			int skip = 1;
			int size = data.getLevels();
			int limit = size / skip + Math.min(size % skip, 1);

			List<LevelData> levelDatas;

			if (tokens.size() < limit) {
				throw new Exception("Zu wenig Level im Datensatz");
			} else {
				List<String> levelDataString = Stream.iterate(beginOfTable, i -> i + skip).limit(limit).map(tokens::get)
						.map(arg -> arg.trim()).map(arg -> arg.contains("---") ? arg.replace("-----", "0") : arg)
						.filter(arg -> arg.length() != 0).collect(Collectors.toList());
				
				levelDatas = levelDataString.stream().map(arg -> new LevelData(arg.split("\\s+")))
						.collect(Collectors.toList());
				data.setLeveldata(levelDatas);
			}
			
			result.setData(data);
		} catch (Exception e) {
			//e.printStackTrace();
			result.setValid(false);
			result.setException(e);

		}
		return result;
	}
	
	private static Sounding splitFirstHeaderLine(String line, Sounding data)
	{		
		Pattern linePattern = Pattern.compile("SOUNDING # [0-9]+\\s+IDN=\\s*(?<id>[0-9]+)\\s+DAY=(?<year>[0-9]{4})(?<day>[0-9]{3})\\s+TIME=\\s*(?<time>[0-9]+)\\s+VALID LEVELS=\\s*(?<lvl>[0-9]+)");
		Matcher lineMatcher = linePattern.matcher(line);
		if (lineMatcher.matches()) {
			data.setStationID(Integer.parseInt(lineMatcher.group("id")));
			data.setDateAndTime(
					LocalDateTime.of(
							LocalDate.ofYearDay(
					Integer.parseInt(lineMatcher.group("year")),
					Integer.parseInt(lineMatcher.group("day"))),
							LocalTime.of(Integer.parseInt(lineMatcher.group("time"))/10000, 0)));
			data.setLevels(Integer.parseInt(lineMatcher.group("lvl")));
			
		}
		return data;
	}
	
	private static Sounding splitSecondHeaderLine(String line, Sounding data)
	{
		Pattern linePattern = Pattern.compile("\\s*[0-9]+\\s*(?<icao>[A-Z]{4})?\\s*(?<station>[A-Za-z-.]*\\s?/?\\s?[A-Za-z.]*\\s?[a-zA-Z.]*)\\s*(?<state>[A-Z]{2})?\\s*(?<country>[A-Z]{2})?\\s*(?<latdeg>[0-9]{2}):(?<latmin>[0-9]{2})(?<lat>[A-Z])\\s*(?<longdeg>[0-9]{3}):(?<longMin>[0-9]{2})(?<long>[A-Z])\\s*(?<elev>[0-9]*)");
		Matcher lineMatcher = linePattern.matcher(line);
		if (lineMatcher.matches()) {
			data.setElevation(Integer.parseInt(lineMatcher.group("elev")));
			double longitude = Double.parseDouble(lineMatcher.group("longdeg"))+Double.parseDouble(lineMatcher.group("longMin"))/60;
			if(lineMatcher.group("long").equals("S"))
				longitude = -longitude;
			data.setLongitude(longitude);
			double latitude = Double.parseDouble(lineMatcher.group("latdeg"))+Double.parseDouble(lineMatcher.group("latmin"))/60;
			if(lineMatcher.group("lat").equals("W"))
				latitude = -latitude;
			data.setLatitude(latitude);
			
		}
		return data;
		//return null;
	}	
}

