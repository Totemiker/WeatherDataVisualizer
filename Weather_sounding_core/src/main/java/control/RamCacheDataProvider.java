package control;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import model.Area;
import model.Reading;
import model.Reading.LevelType;
import model.Sounding;
import model.Station;
import model.StationId;

public class RamCacheDataProvider extends ChainedDataProvider {
	
	Multimap<Area, StationId> areasToStations = HashMultimap.create();
	
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
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Optional<Reading> getReading(StationId station, LocalDateTime time, LevelType type) {
		// if sounding is cached
			// return reading from sounding
		// else load sound, but do not cache, and return reading
		return null;
	}

}
