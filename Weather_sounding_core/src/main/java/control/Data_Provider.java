package control;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import model.Area;
import model.LevelData;
import model.Sounding;
import model.Station;
import model.WeatherDataParser;
import model.WeatherDataParserValidationResult;
import model.LevelData.LevelType;

/**
 * Diese Klasse soll alle Daten zur Verfügung stellen
 * 
 * @author Tobias Teumert
 * 
 */

public class Data_Provider implements DataProviderInterface {
	private static final List<Area> areas = new ArrayList<>();
	private final List<Station> stations = new ArrayList<>();
	private Properties properties;

	public Data_Provider(Properties properties) {
		this.properties = properties;
		areas.add(new Area("eur", "Europa"));
		areas.add(new Area("fr", "France"));
		areas.add(new Area("qc", "Quebec"));
		areas.add(new Area("uk", "UK"));
		areas.add(new Area("ca", "Canada"));
		areas.add(new Area("us", "USA"));
	}

	@Override
	public List<Area> getAreas() {
		return areas;
	}

	@Override
	public List<Station> getStationsByArea(Area area) {

		Document doc;
		doc = null;
		try {
			doc = Jsoup.connect(properties.getProperty("weblink") + "area=" + area.getAreaCode()).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Elements e = doc.select("select[name=\"stn\"]").select("option");
		// e.forEach(System.out::println);
		return e.stream()
				.map(element -> new Station(Integer.parseInt(element.attributes().get("value")), element.text()))
				.collect(Collectors.toList());
	}

	@Override
	public void setStations(List<Station> stations) {
		this.stations.clear();
		this.stations.addAll(stations);

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

	/**
	 * 
	 * @param station   The Station to get the Sounding from
	 * @param time      The specific time of the Sounding to be generated
	 * @param area      The Area in which the Station lies
	 * @return The RawData of the specified Sounding
	 */
	@Override
	public Sounding getSounding(Station station, LocalDateTime time, Area area) {
		if (soundingsByStation.get(station).stream()
				.anyMatch(sound -> sound.getDateAndTime().isEqual(time))) {
			System.out.println("Found Sounding in Multimap");
			return soundingsByStation.get(station).stream()
					.filter(sound -> sound.getDateAndTime().isEqual(time))
					.findFirst()
					.get();
		} else {
			System.out.println("Get Sounding from net");
			
			String urlBuilder = "http://meteocentre.com/radiosonde/get_sounding.php?lang=en&show=0&hist=0&area="
					+ area.getAreaCode() + "&stn=" 
					+ station.getStationID() + "&type=txt&yyyy="
					+ time.getYear()+ "&mm="
					+ time.getMonthValue() + "&dd=" 
					+ time.getDayOfMonth() + "&run=" 
					+ time.getHour();
			
			Document doc = null;
			
			try {
				doc = Jsoup.connect(urlBuilder).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			WeatherDataParserValidationResult result = WeatherDataParser.validate(doc.select("pre").first().text());
			
			if (result.isValid())
			{
				System.out.println("Vaild Result from parser");
				soundingsByStation.put(station, result.getData());
				return result.getData();				
			}
			else
				{
				System.out.println("No Valid Result from Parser");
				System.out.println(result.getException());
				//result.getException().printStackTrace();
				result.getData().setDateAndTime(time);
				result.getData().setStationID(station.getStationID());
				return result.getData();
				}
		}
	}

	@Override
	public List<Sounding> getSoundingsByStation(Station station, LocalDateTime start, LocalDateTime ende, Area area) {
		// TODO Überprüfen ob alle Soundings schon geladen sind
		// TODO Wenn ja, einfache zurückgabe der gewünschten soundings
		// TODO Wenn nein einlesen der Daten aus dem Web
		
		List<Sounding> temp = new ArrayList<Sounding>();
		LocalDateTime increase = start;
		while(increase.isBefore(ende) || increase.isEqual(ende))
		{
			temp.add(getSounding(station, increase, area));
			increase = increase.plusHours(12);
		}
		
		return temp;
	}
	
	/**
	 * 
	 * @return A List of available Values
	 */
	public List<PlotCommand> getAvailableSoundingValues()
	{
		List<PlotCommand> values = new ArrayList<PlotCommand>();
		
		values.add(new PlotCommand("Temperature", LevelData::getTemp));
		values.add(new PlotCommand("Dew Point", LevelData::getDewPoint));
		values.add(new PlotCommand("Wind Direction", LevelData::getDirection));
		values.add(new PlotCommand("Wind Speed", LevelData::getWindspeed));
		values.add(new PlotCommand("Height", LevelData::getHeight));
		values.add(new PlotCommand("Theta", LevelData::getTheta));
		values.add(new PlotCommand("Mix", LevelData::getMix));	
		
		return values;
	}	
	
	/**
	 * 
	 * @return A List of available Pressure Levels to Plot
	 */
	public List<LevelType> getAvailablePressureLevels()
	{
		return List.of(LevelType.values());
	}
}
