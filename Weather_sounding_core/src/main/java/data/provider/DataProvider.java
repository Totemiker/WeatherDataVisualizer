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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import data.model.Area;
import data.model.Reading;
import data.model.Sounding;
import data.model.StationId;
import data.model.Reading.LevelType;
import data.model.WeatherDataParser.StationBuilder;
/**
 * Skeleton for data provision. Defines a preset of methods which have to be present.<br>
 * Extends StationBuilder to present implementing classes a method of creating a Station
 * @author Tobias
 *
 */
public interface DataProvider extends StationBuilder 
{
		/**<code>List</code> of Areas which are available */
		public List<Area> getAreas();
		/**<code>List</code> of StationIDs in a given Area
		 * 
		 * @param area The area to get the StationID´s from
		 * @return A List containing the StationID´s of the area
		 */
		public List<StationId> getStationsByArea(Area area);
		
		//TODO Optional?
		/**Getter for Sounding from a specified StationID and a specified Time
		 * 
		 * @param station	The stationID to get the Sounding from
		 * @param time		The Time of the Sounding
		 * @return			The Sounding to the specified parameters
		 */
		public Sounding getSounding(StationId station, LocalDateTime time);
		
		//TODO Optional?
		/**Getter for Multiple Soundings from a specified stationID in the timeframe start - end
		 * 
		 * @param station	The stationID to get the Sounding from
		 * @param start		The start time of the interval
		 * @param end		The end time of the interval
		 * @return			A List containing all Soundings of the given Station in the given timeframe
		 */
		public List<Sounding> getSoundings(StationId station, LocalDateTime start, LocalDateTime end);
		
		/**
		 * Getter for a Reading from one Sounding
		 * @param station	The StationID to get the Reading from
		 * @param time		The Time of the Reading
		 * @param type		The Leveltype of the Reading
		 * @return			An Optional with either the Reading if the Sounding contains it or null if not
		 */
		public Optional<Reading> getReading(StationId station, LocalDateTime time, LevelType type);
		
		/**
		 * Getter for multiple Readings in a given timeframe
		 * @param station	The StationID to get the Reading from
		 * @param start		The start time of the interval
		 * @param end		The end time of the interval
		 * @param type		The Leveltype of the Reading
		 * @return			a List of Optionals containig Readings or null
		 */
		public List<Optional<Reading>> getReadings(StationId station, LocalDateTime start, LocalDateTime end, LevelType type);
}
