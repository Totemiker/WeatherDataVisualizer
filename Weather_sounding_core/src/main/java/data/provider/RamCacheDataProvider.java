package data.provider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.WeakHashMap;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import data.model.Area;
import data.model.Reading;
import data.model.ReadingKey;
import data.model.Sounding;
import data.model.Station;
import data.model.StationId;
import data.model.Reading.LevelType;

public class RamCacheDataProvider extends ChainedDataProvider {
	
	Multimap<Area, StationId> areasToStations = HashMultimap.create();
	
	WeakHashMap<ReadingKey, Reading> storedReadings = new WeakHashMap<>();
	
	public RamCacheDataProvider(DataProvider provider, Properties prop) {
		this.properties = prop;
		this.upstream = provider;		
		
	}
	
	@Override
	public List<StationId> getStationsByArea(Area area) {
		if(areasToStations.containsKey(area))
			return new ArrayList<>(areasToStations.get(area));
		else
		{
			List<StationId> stations = null;
			stations = upstream.getStationsByArea(area);
			if(stations != null)
			{
				areasToStations.putAll(area,stations);
				return stations;
			}			
		}
		return null;
	}

	@Override
	public Sounding getSounding(StationId station, LocalDateTime time) {
		Sounding sounding = null;
		if ((sounding = upstream.getSounding(station, time)) != null)
			return sounding;
		return null;
	}

	@Override
	public Station buildStation(StationId stationID, String stationName, double longi, double lati, int elevation,
			String icao) {
		Station station = new Station(stationID, stationName, longi, lati, elevation, icao);
		return stations.stream().filter(arg -> arg.equals(station)).findFirst().orElse(station);
	}
	
	@Override
	public Optional<Reading> getReading(StationId station, LocalDateTime time, LevelType type) {
		ReadingKey key = new ReadingKey(station, time, type);
		if(storedReadings.containsKey(key))
		{
			return Optional.of(storedReadings.get(key));
		}
		else
		{
			Optional<Reading> opt = upstream.getReading(station, time, type);
			if(opt.isPresent())
				storedReadings.put(key, opt.get());
			return opt;
		}
		
	}
	
}
