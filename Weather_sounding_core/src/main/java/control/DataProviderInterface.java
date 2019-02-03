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
		/**
		 * Contains every selected Sounding from programstart
		 */
		public final Multimap<Station,Sounding> soundingsByStation  = MultimapBuilder.hashKeys().arrayListValues().build();
		
		//public final List<Double> mandatoryLevels =  Collections.unmodifiableList(Arrays.asList(925.0,850.0,700.0,500.0,400.0,300.0,250.0,200.0,150.0,100.0,70.0,50.0,20.0,10.0));
		
		//public final List<String> levelTypes = Collections.unmodifiableList(Arrays.asList("SFC","MAXW","TRO1","TRO2"));
		//ublic final Multimap<Station,Sounding> workingOnSoundings = MultimapBuilder.hashKeys().arrayListValues().build();
		
		public List<Area> getAreas();
		
		public List<Station> getStationsByArea(Area area);
		
		public void setStations(List<Station> stations);
		
		public Sounding getSounding(Station station, LocalDateTime time, Area area);
		
		public List<Sounding> getSoundingsByStation(Station station, LocalDateTime start, LocalDateTime ende, Area area);
		
		public List<Double> getMandatorySoundingLevel(Sounding sound);
		
		
		
		//public void importSounding();
}
