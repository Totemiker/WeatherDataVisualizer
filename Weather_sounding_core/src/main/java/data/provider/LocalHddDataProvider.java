/*
 * Copyright (c) 2019 Tobias Teumert
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package data.provider;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

import data.model.Area;
import data.model.Sounding;
import data.model.Station;
import data.model.StationId;
import data.model.WeatherDataParser;
import data.model.WeatherDataParserValidationResult;

/**
 * The HDD data provider<BR>
 * Extends ChainedDataProvider to be able to get data by another Provider if not locally stored 
 * @author Tobias
 *
 */
public class LocalHddDataProvider extends ChainedDataProvider{

	WeatherDataParser parser;
	
	/**
	 * 
	 * File Structure <br><code>property\\$region_Code\\$Station_Code\\$LocalDateTime.txt</code>
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
		
		String datadir = ""+properties.get("data_dir");
		
		//create Path to look on HDD
		Path regionP = Path.of(datadir)
				.resolve(""+station.getArea().getAreaCode())
				.resolve(""+station.getStationID())
				.resolve(""+time.toLocalDate().format(DateTimeFormatter.ISO_DATE)+"T"+time.getHour()+".txt");
		
		if(Files.exists(regionP))//Region Path exists
		{
			
			
			try (BufferedReader br =
	                   new BufferedReader(new FileReader(regionP.toFile(),Charset.forName("UTF-8")))){
				
				List<String> tokens = br.lines().collect(Collectors.toList());
				
				WeatherDataParserValidationResult result = parser.validate(tokens.stream().collect(Collectors.joining("\r\n")), station);
				
				if (result.isValid()) {
					//FIXME Rewrite to Logger
					System.out.println("HDD - Vaild Result from parser");
					//soundingsByStation.put(station, result.getData());
					stations.add(result.getData().getStation());
					return result.getData();
				} else {
					System.out.println("HDD - No Valid Result from Parser");
					System.out.println(result.getException());				
					result.getData().setDateAndTime(time);
					return result.getData();
				}
							
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			// if local data is corrupted use this to get new from upstream
			System.out.println("HDD - Upstream");
			return upstream.getSounding(station, time);
		}
		else // no data on HDD get from upstream
		{
			System.out.println("Get from web");
			return upstream.getSounding(station, time);
			
		}
	}

	@Override
	public Station buildStation(StationId stationID, String stationName, double longi, double lati, int elevation,
			String icao) {
		
		Station station = new Station(stationID, stationName, longi, lati, elevation, icao);
		//checks wether the station is already contained in the stations-Set
		return stations.stream().filter(arg -> arg.equals(station)).findFirst().orElse(station);
		
	}
	/**
	 * Writes a string containing a sounding to a file on HDD
	 * @param raw	Raw string to be saved 
	 * @param ldt	time to construct path
	 * @param id	id of station to construct path
	 */
	public void cacheRawSounding(String raw,LocalDateTime ldt,StationId id)
	{
		String datadir = ""+properties.get("data_dir");
		//Construct path to save to HDD
		Path toSave = Path.of(datadir)
				.resolve(""+id.getArea().getAreaCode())
				.resolve(""+id.getStationID())
				.resolve(""+ldt.toLocalDate().format(DateTimeFormatter.ISO_DATE)+"T"+ldt.getHour()+".txt");
		//FIXME Try-close block
		try {
			
			Path p1 = Path.of(datadir).resolve(id.getArea().getAreaCode());
			if(!Files.exists(p1)) //Checks if Directory of area exists
				Files.createDirectories(p1);
			Path p2 = p1.resolve(""+id.getStationID());
			if(!Files.exists(p2)) // Checks if directory of stationID exists
				Files.createDirectory(p2);
			
			Files.createFile(toSave);
			BufferedWriter writer = Files.newBufferedWriter(toSave);
			writer.write(raw);
			writer.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}	

}
