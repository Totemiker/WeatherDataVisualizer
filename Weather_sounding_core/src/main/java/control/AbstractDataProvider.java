package control;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import model.Area;
import model.Reading;
import model.Reading.LevelType;
import model.Sounding;
import model.Station;
import model.StationId;

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
