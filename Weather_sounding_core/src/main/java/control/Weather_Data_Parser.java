package control;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.LevelData;
import model.Sounding;
import model.Station;

public class Weather_Data_Parser 
{
	private String stationShort,stationName;
	
	public Sounding generateSounding(String toParse)
	{
		StringReader reader = new StringReader(toParse);
		BufferedReader buf = new BufferedReader(reader);
		
		//System.out.println("Zu Bearbeiten\n" + toParse);
			
		List<String> tokens = buf.lines().collect(Collectors.toList());
		
		System.out.println("\nAb hier Tokens: " + tokens.size()+" \n");
		//tokens.forEach(arg -> System.out.println(arg));
		
		List<String> zeile2,zeile5;
		
		zeile2 = Arrays.asList(tokens.get(2).split("[ ]+"));
		zeile5 = Arrays.asList(tokens.get(5).split("[ ]+"));
		
		System.out.println("Zeile 2 " + zeile2);
		System.out.println("Zeile 5 " + zeile5);
		
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
		
		System.out.println("Datum "+p1+" "+p2+" "+ldt+"\n");
				
		stationName = zeile5.get(3);	
		stationShort = zeile5.get(2);
		String countryID = zeile5.get(4);
		
		
		//int stationID = Integer.parseInt(zeile2.get(4));
		int elevation = Integer.parseInt(zeile2.get(9));
		
		String latstr = zeile5.get(5);
		String[] temp = latstr.split(":");
		System.out.println("Lat "+temp[0] +" / "+ temp[1]);
		
		//Decimal Degrees = Degrees + minutes/60
		
		String lonstr = zeile5.get(6);
		String[] temp2 = lonstr.split(":");		
		
		double latitude = Double.parseDouble(temp[0]) + (Double.parseDouble(temp[1].substring(0, 2))/60D);
		double longitude = Double.parseDouble(temp2[0]) + (Double.parseDouble(temp2[1].substring(0,2))/60D);
				
		Station station = new Station(stationName, stationShort, countryID,longitude ,latitude ,stationID ,elevation);
		
		int levels = Integer.parseInt(zeile2.get(9));
		
		int beginOfTable = 12;
		int skip = 1;
		int size = levels;
		int limit = size / skip + Math.min(size % skip, 1);
		
	
		
		// a ? b : c = if a return b else return c
		
	
		List<String> levelDataString = Stream.iterate(beginOfTable, i -> i+skip)
				.limit(limit)
				.map(tokens::get)
				.map(arg -> arg.trim())
				.map(arg -> arg.contains("---") ? arg.replace("-----", "0") : arg)
				.filter(arg -> arg.length() != 0)
				.collect(Collectors.toList());			
		
		List<LevelData> levelDatas = levelDataString.stream().map(arg -> new LevelData(arg.split("\\s+"))).collect(Collectors.toList());		
		
		return new Sounding(station, ldt, levelDatas);	
		
	}
}