package control;

import java.time.LocalDateTime;
import java.util.List;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import model.Area;
import model.Sounding;
import model.Station;

public interface DataProviderInterface 
{
		public final Multimap<Station,Sounding> soundingsByStation  = MultimapBuilder.hashKeys().arrayListValues().build();
	
		public List<Area> getAreas();
		
		public List<Station> getStationsByArea(Area area);
		
		public void setStations(List<Station> stations);
		
		public Sounding getSounding(Station station, LocalDateTime time, Area area);
		
		public List <Sounding> getSoundingsByStation(int stationID, LocalDateTime start, LocalDateTime ende);
		
		//public void importSounding();
}
