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
package data.model;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import data.model.Reading.LevelType;

/**
 * Parser to parse a supplied String into data objects 
 * @author Tobias
 *
 */
public class WeatherDataParser {

	/**
	 * Functional Interface to build a station from given values
	 * @author Tobias
	 *
	 */
	@FunctionalInterface
	public static interface StationBuilder {
		/**
		 * builder for Station
		 * @param stationID
		 * @param stationName
		 * @param longi
		 * @param lati
		 * @param elevation
		 * @param icao
		 * @return
		 */
		public Station buildStation(StationId stationID, String stationName, double longi, double lati, int elevation,
				String icao);
	}
	
	// (StationId, String, double, double, int, String) -> Station
	/**
	 * Can be used to supply stations e.g. from cache.
	 * Creates a new Station
	 */
	StationBuilder stationSupplier = Station::new;

	public WeatherDataParser(StationBuilder builder) {
		this.stationSupplier = builder;
	}

	/**
	 * Validates a given input string to a ValidationResult which either contains valid data or,
	 * <p>if data is corrupted or no data is available an empty result
	 * 
	 * @param toParse String containing the data. Can contain no data.
	 * @return The validation result. It contains the validated Data (Sounding) and in case of
	 *         no valid data the type of exception and a boolean (valid)
	 */
	public WeatherDataParserValidationResult validate(String toParse, StationId statID) {
		WeatherDataParserValidationResult result = new WeatherDataParserValidationResult();
		Sounding data = new Sounding();
		try {

			StringReader reader = new StringReader(toParse);
			BufferedReader buf = new BufferedReader(reader);

			List<String> tokens = buf.lines().collect(Collectors.toList());

			if (tokens.stream()
					.anyMatch(element -> element.contains("No observations found for selection conditions"))) {
				data.setStationId(statID);
				data.setStation(stationSupplier.buildStation(statID, "", 0, 0, 0, ""));
				throw new Exception("No observations found for selection conditions");
			}
			if (tokens.size() < 5) {
				data.setStationId(statID);
				data.setStation(stationSupplier.buildStation(statID, "", 0, 0, 0, ""));
				throw new Exception("To few level in Data Set - presumably corrupted data ");
			}

			splitFirstHeaderLine(tokens.get(2), data);
			data.setStation(splitSecondHeaderLine(tokens.get(5), statID));
			data.setStationId(statID);

			List<Reading> levelDatas;

			String dpat = "([-+]?[0-9]*\\.?[0-9]+||-{2,})";
			Pattern pattern = Pattern.compile("\\s*([A-Z]+[0-9]?)\\s*" + dpat + "\\s*" + dpat + "\\s*" + dpat + "\\s*"
					+ dpat + "\\s*" + dpat + "\\s*" + dpat + "\\s*" + dpat + "\\s*" + dpat);

			List<String> levelDataString = tokens.stream()
					.filter(arg -> pattern.matcher(arg).matches()) 
					.map(arg -> arg.trim())
					.map(arg -> arg.contains("---") ? arg.replace("-----", "0") : arg)
					.filter(arg -> arg.length() != 0).collect(Collectors.toList());

			levelDatas = levelDataString.stream().map(arg -> createReading(arg.split("\\s+")))
					.collect(Collectors.toList());

			data.setReadings(levelDatas);

			result.setData(data);
		} catch (Exception e) {
			// e.printStackTrace();
			result.setValid(false);
			result.setException(e);
			result.setData(data);
			return result;

		}
		return result;
	}

	/**
	 * Creates the correct Leveltype in the Reading
	 * 
	 * @param split the Array to parse as Reading
	 * @return the parsed Reading
	 */
	private static Reading createReading(String[] split) {
		LevelType type = null;
		if (split[0].equals("MAND")) {
			split[0] = "MAND_" + ((int) Double.parseDouble(split[1]));
			type = LevelType.valueOf(split[0]);
		} else if (split[0].equals("TRO1") || split[0].equals("TRO2") || split[0].equals("SFC")
				|| split[0].equals("MAXW"))
			type = LevelType.valueOf(split[0]);
		else
			type = LevelType.CUSTOM;

		Reading data = new Reading(split, type);
		return data;
	}

	/**
	 * Splits the first Stationdata line of a sounding
	 * 
	 * @param line 	The String to analyse (A line from a Sounding)
	 * @param data	The corresponding Sounding
	 * @return		The manipulated Sounding
	 */
	private static Sounding splitFirstHeaderLine(String line, Sounding data) {
		Pattern linePattern = Pattern.compile(
				"SOUNDING # [0-9]+\\s+IDN=\\s*(?<id>[0-9]+)\\s+DAY=(?<year>[0-9]{4})(?<day>[0-9]{3})\\s+TIME=\\s*(?<time>[0-9]+)\\s+VALID LEVELS=\\s*(?<lvl>[0-9]+)");
		Matcher lineMatcher = linePattern.matcher(line);
		if (lineMatcher.matches()) {
			data.setDateAndTime(LocalDateTime.of(
					LocalDate.ofYearDay(Integer.parseInt(lineMatcher.group("year")),
							Integer.parseInt(lineMatcher.group("day"))),
					LocalTime.of(Integer.parseInt(lineMatcher.group("time")) / 10000, 0)));
		}
		return data;
	}
	/**
	 * Splits the second station data line of a sounding 
	 * @param line	The string to analyse
	 * @param id	the corresponding Station ID
	 * @return	The Station which is identified by the input data
	 */
	private Station splitSecondHeaderLine(String line, StationId id) {
		int elevation = 0;
		double longitude = 0, latitude = 0;
		String icao = "", stationName = "";
		Pattern linePattern = Pattern.compile(
				"\\s*[0-9]+\\s*(?<icao>[A-Z]{4})?\\s*(?<station>[A-Za-z-.]*\\s?/?\\s?[A-Za-z.]*\\s?[a-zA-Z.]*)\\s*(?<state>[A-Z]{2})?\\s*(?<country>[A-Z]{2})?\\s*(?<latdeg>[0-9]{2}):(?<latmin>[0-9]{2})(?<lat>[A-Z])\\s*(?<longdeg>[0-9]{3}):(?<longMin>[0-9]{2})(?<long>[A-Z])\\s*(?<elev>[0-9]*)");
		Matcher lineMatcher = linePattern.matcher(line);
		if (lineMatcher.matches()) {
			elevation = Integer.parseInt(lineMatcher.group("elev"));
			longitude = Double.parseDouble(lineMatcher.group("longdeg"))
					+ Double.parseDouble(lineMatcher.group("longMin")) / 60;
			if (lineMatcher.group("long").equals("S"))
				longitude = -longitude;

			latitude = Double.parseDouble(lineMatcher.group("latdeg"))
					+ Double.parseDouble(lineMatcher.group("latmin")) / 60;
			if (lineMatcher.group("lat").equals("W"))
				latitude = -latitude;

			icao = lineMatcher.group("icao");
			stationName = lineMatcher.group("station");

		}
		return stationSupplier.buildStation(id, stationName, longitude, latitude, elevation, icao);
		// return null;
	}

}
