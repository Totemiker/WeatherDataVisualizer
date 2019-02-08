package control;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.WeakHashMap;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import model.Area;
import model.Reading;
import model.Reading.LevelType;
import model.ReadingKey;
import model.Sounding;
import model.Station;
import model.StationId;

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
			System.out.println("Contains Reading in RAMCache");
			return Optional.of(storedReadings.get(key));
		}else
		{
			System.out.println("Reading in Sounding and storing and returning Reading");
			Optional<Reading> opt = getSounding(station,time).getReadings().stream().filter(arg -> arg.getLevel().equals(type)).findFirst();
			
			if(opt.isPresent())
				storedReadings.put(key, opt.get());
			else
			{
				int zähler = 1;
				if (zähler <=10)
				{
					opt = getSounding(station,time.minusDays(zähler)).getReadings().stream().filter(arg -> arg.getLevel().equals(type)).findFirst();
					if(opt.isPresent())
						storedReadings.put(key, opt.get());
					zähler++;
					System.out.println("Versuche altes Reading zu besorgen");
				}
				else
				{
					System.out.println("Hier noch was überlegen");
				}
				//other Reading
			}
			
			
			return opt;
		}
		
		// if sounding is cached
			// return reading from sounding
		// else load sound, but do not cache, and return reading
		//return null;
	}
}
