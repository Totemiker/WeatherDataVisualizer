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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import data.model.Area;
import data.model.Sounding;
import data.model.Station;
import data.model.StationId;
import data.model.WeatherDataParser;
import data.model.WeatherDataParserValidationResult;

/**
 * Web Data Provider<br>
 * Fetches Data from given URL and parses the Input into the Model
 * 
 * @author Tobias Teumert
 * 
 */

public class WebDataProvider extends ChainedDataProvider {
	
	/** The Parser*/		
	private WeatherDataParser parser;
	
	/** The applied Caching Strategy*/
	private RawCachingStrategy strategy;
	

	public WebDataProvider(Properties properties) {
		this.properties = properties;
		parser = new WeatherDataParser(this::buildStation);
		this.strategy = (String s, LocalDateTime ldt ,StationId id) -> {}; //no - op
		
	}
	
	public void setRawCachingStrategy(RawCachingStrategy strat)
	{
		this.strategy = strat;
	}
	
	
	
	
	@Override
	public List<StationId> getStationsByArea(Area area) {

		Document doc;
		doc = null;
		try {
			doc = Jsoup.connect(properties.getProperty("weblink") + "area=" + area.getAreaCode()).get();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		Elements e = doc.select("select[name=\"stn\"]").select("option");
		// e.forEach(System.out::println);
		return e.stream()
				.map(element -> new StationId(Integer.parseInt(element.attributes().get("value")), element.text(), area))
				.collect(Collectors.toList());
	}
	
	
	@Override
	public Sounding getSounding(StationId station, LocalDateTime time) {
		
		System.out.println("Get Sounding from net");
		Document doc = null;
		
		try {
			doc = Jsoup.connect( "http://meteocentre.com/radiosonde/get_sounding.php")
				.data("lang", "en")
				.data("type", "txt")
				.data("area", station.getArea().getAreaCode())
				.data("stn", ""+String.format("%05d",station.getStationID()))
				.data("yyyy", ""+time.getYear())
				.data("mm", ""+time.getMonthValue())
				.data("dd", ""+time.getDayOfMonth())
				.data("run", (time.getHour() == 0 ? "00": ""+time.getHour()))
				.get();			
			
		} catch (IOException e) {
			e.printStackTrace();
			//TODO Handling of no Internet connection
			return null;
		}

		WeatherDataParserValidationResult result = parser.validate(doc.select("pre").first().text(), station);

		if (result.isValid()) {
			
			stations.add(result.getData().getStation());			
			
			strategy.cacheRawSounding(doc.select("pre").first().text(), time, station);
			return result.getData();
		} else {
			System.out.println("WDP - No Valid Result from Parser");
			System.out.println(result.getException());
			
			result.getData().setDateAndTime(time);			
			return result.getData();
		}

	}
	/**
	 * The caching strategy
	 * @author Tobias
	 *
	 */
	@FunctionalInterface
	public static interface RawCachingStrategy{
		
		void cacheRawSounding(String raw,LocalDateTime ldt, StationId id);
	}

	@Override
	public Station buildStation(StationId stationID, String stationName, double longi, double lati, int elevation,
			String icao) {
		Station station = new Station(stationID, stationName, longi, lati, elevation, icao);
		return stations.stream().filter(arg -> arg.equals(station)).findFirst().orElse(station);
	}
}
