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

public interface DataProvider extends StationBuilder 
{
	
		public List<Area> getAreas();
		
		public List<StationId> getStationsByArea(Area area);
		
		public Sounding getSounding(StationId station, LocalDateTime time);
		
		public List<Sounding> getSoundings(StationId station, LocalDateTime start, LocalDateTime end);
		
		public Optional<Reading> getReading(StationId station, LocalDateTime time, LevelType type);
		
		public List<Optional<Reading>> getReadings(StationId station, LocalDateTime start, LocalDateTime end, LevelType type);
}
