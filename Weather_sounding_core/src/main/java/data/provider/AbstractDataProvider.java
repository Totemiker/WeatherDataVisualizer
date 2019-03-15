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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import data.model.Area;
import data.model.Reading;
import data.model.Sounding;
import data.model.Station;
import data.model.StationId;
import data.model.Reading.LevelType;

/**
 * 
 * @author Tobias
 *
 */
public abstract class AbstractDataProvider implements DataProvider {
	
	protected Properties properties;
	private static final List<Area> areas = new ArrayList<>();
	protected final Set<Station> stations = new HashSet<>();
	
	static {
		areas.add(new Area("eur", "Europa"));
		areas.add(new Area("fr", "France"));
		areas.add(new Area("qc", "Quebec"));
		areas.add(new Area("uk", "UK"));
		areas.add(new Area("ca", "Canada"));
		areas.add(new Area("us", "USA"));
	}
	
	@Override
	public List<Sounding> getSoundings(StationId station, LocalDateTime start, LocalDateTime ende) {
		List<Sounding> temp = new ArrayList<Sounding>();		
		LocalDateTime increase = start;	
		
		while(increase.isBefore(ende) || increase.isEqual(ende))
		{
			temp.add(getSounding(station, increase));
			increase = increase.plusDays(1);
		}
		return temp;
	}
	
	@Override
	public List<Optional<Reading>> getReadings(StationId station, LocalDateTime start, LocalDateTime ende, LevelType type) {
		
		List<Optional<Reading>> temp = new ArrayList<Optional<Reading>>();		
		LocalDateTime increase = start;	
		
		while(increase.isBefore(ende) || increase.isEqual(ende))
		{
			temp.add(getReading(station, increase, type));
			increase = increase.plusDays(1);
		}
		return temp;
	}
	
	@Override
	public Optional<Reading> getReading(StationId station, LocalDateTime time, LevelType type) {
		return getSounding(station, time).getReadings().stream().filter(arg -> arg.getLevel().equals(type)).findFirst();
	}
	
	@Override
	public List<Area> getAreas() {
		return areas;
	}
	
	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	
	
	
}
