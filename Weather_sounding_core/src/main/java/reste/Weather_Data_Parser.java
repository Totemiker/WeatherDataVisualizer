package reste;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.LevelData;
import model.Sounding;
import model.Station;

public class Weather_Data_Parser 
{		
	Sounding data;
			
	public Sounding getData()
	{
		return data;
	}
	
	public void validate(String toParse, String region)
	{		
		StringReader reader = new StringReader(toParse);
		BufferedReader buf = new BufferedReader(reader);		
			
		List<String> tokens = buf.lines().collect(Collectors.toList());
		
		if(tokens.stream().anyMatch(element -> element.contains("No observations found for selection conditions")))
		{
			System.out.println("Fehler bei Dateneinlesen");
			//Fehlerbehandlung
			data = null;
			return;
		}
				
		List<String> zeile2,zeile5;
		
		zeile2 = Arrays.asList(tokens.get(2).split("[ ]+"));
		zeile5 = Arrays.asList(tokens.get(5).split("[ ]+"));
		
		//System.out.println("Zeile 2 " + zeile2);
		//System.out.println("Zeile 5 " + zeile5);
		
		int stationID = Integer.parseInt(zeile5.get(1));
		
		//Extraction DateAndTime
		String tempday = zeile2.get(5);
		String[] arr = tempday.split("=");
		tempday = arr[1];
		String p1 ="";
		String p2 ="";
		p1 = tempday.substring(0, 4);
		p2 = tempday.substring(5, 7);
		LocalDate ld = LocalDate.ofYearDay(Integer.parseInt(p1), Integer.parseInt(p2));
						
		String temptime = zeile2.get(6);
		String[] arr2 = temptime.split("=");
		tempday = arr2[1];		
		LocalTime time;
		if(tempday.equals("0"))
			time = LocalTime.of(0, 0);
		else
			time = LocalTime.of(12, 0);
		
		LocalDateTime ldt = LocalDateTime.of(ld, time);
		
		System.out.println("Datum "+ldt+"\n");
				
		String stationName = zeile5.get(3);	
		String stationShort = zeile5.get(2);
		String countryID = zeile5.get(4);
		
		int elevation = Integer.parseInt(zeile2.get(9));
		
		String latstr = zeile5.get(5);
		String lonStr = zeile5.get(6);
		
		Pattern latPat = Pattern.compile("([0-9]+):([0-9]+)(N|S)");
		Matcher latMat = latPat.matcher(latstr);
		double latitude = 0;
		if(latMat.matches())
		{			
			String first = latMat.group(1);
			String second = latMat.group(2);
			String third = latMat.group(3);
			//System.out.println("1st "+first + " 2nd "+second+ " 3rd "+ third);
			
			latitude = Double.parseDouble(first) + (Double.parseDouble(second)/60D);
			if(third.equals("S"))
				latitude = -latitude;
		}	
		
		Pattern lonPat = Pattern.compile("([0-9]+):([0-9]+)(E|W)");
		Matcher lonMat = lonPat.matcher(lonStr);
		double longitude = 0;
		if(lonMat.matches())
		{			
			String first = lonMat.group(1);
			String second = lonMat.group(2);
			String third = lonMat.group(3);
			//System.out.println("1st "+first + " 2nd "+second+ " 3rd "+ third);
			
			longitude = Double.parseDouble(first) + (Double.parseDouble(second)/60D);
			if(third.equals("W"))
				longitude = -longitude;
		}		
				
		Station station = new Station(stationName, stationShort, countryID, region,longitude ,latitude ,stationID ,elevation);
		
		int levels = Integer.parseInt(zeile2.get(9));
		
		int beginOfTable = 12;
		int skip = 1;
		int size = levels;
		int limit = size / skip + Math.min(size % skip, 1);					
		
		if(tokens.size()<limit)
		{
			System.out.println("Zu wenig Level im Datensatz");
			data = null;
			return;
		}
		else {
			List<String> levelDataString = Stream.iterate(beginOfTable, i -> i+skip)
					.limit(limit)
					.map(tokens::get)
					.map(arg -> arg.trim())
					.map(arg -> arg.contains("---") ? arg.replace("-----", "0") : arg)
					.filter(arg -> arg.length() != 0)
					.collect(Collectors.toList());			
			
			List<LevelData> levelDatas = levelDataString.stream().map(arg -> new LevelData(arg.split("\\s+"))).collect(Collectors.toList());		
			
			//data = new Sounding(station, ldt, levelDatas);
		}
		
					
	}
}