package control;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import model.Area;
import model.Sounding;
import model.Station;
import model.StationId;
import model.WeatherDataParser;
import model.WeatherDataParserValidationResult;

/**
 * Diese Klasse soll alle Daten aus dem Web ziehen und zur Verfügung stellen
 * 
 * @author Tobias Teumert
 * 
 */

public class WebDataProvider extends ChainedDataProvider {
	
	//private final Multimap<StationId,Sounding> soundingsByStation  = MultimapBuilder.hashKeys().arrayListValues().build();
		
	private WeatherDataParser parser;

	public WebDataProvider(Properties properties) {
		this.properties = properties;
		parser = new WeatherDataParser(this::buildStation);
	}
	
	/**
	 * Internet Liste der Stationen
	 */
	@Override
	public List<StationId> getStationsByArea(Area area) {

		Document doc;
		doc = null;
		try {
			doc = Jsoup.connect(properties.getProperty("weblink") + "area=" + area.getAreaCode()).get();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		Elements e = doc.select("select[name=\"stn\"]").select("option");
		// e.forEach(System.out::println);
		return e.stream()
				.map(element -> new StationId(Integer.parseInt(element.attributes().get("value")), element.text(), area))
				.collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @param stationId	The Station to get the Sounding from
	 * @param time		The specific time of the Sounding to be generated
	 * @param area      The Area in which the Station lies
	 * @return The RawData of the specified Sounding
	 */
	@Override
	public Sounding getSounding(StationId station, LocalDateTime time) {
		
		/*
		 * if (soundingsByStation.get(station).stream() .anyMatch(sound ->
		 * sound.getDateAndTime().isEqual(time))) {
		 * System.out.println("Found Sounding in Multimap"); return
		 * soundingsByStation.get(station).stream() .filter(sound ->
		 * sound.getDateAndTime().isEqual(time)) .findFirst() .get(); } else
		 */

		System.out.println("Get Sounding from net");

		/*String urlBuilder = "http://meteocentre.com/radiosonde/get_sounding.php?lang=en&show=0&hist=0&area="
				+ station.getArea().getAreaCode() + "&stn=" + station.getStationID() + "&type=txt&yyyy="
				+ time.getYear() + "&mm=" + time.getMonthValue() + "&dd=" + time.getDayOfMonth() + "&run="
				+ time.getHour();*/

		Document doc = null;
		
		//System.out.println("Station ID"+station.getStationID());

		try {
			doc = Jsoup.connect( "http://meteocentre.com/radiosonde/get_sounding.php")
				.data("lang", "en")
				.data("type", "txt")
				.data("area", station.getArea().getAreaCode())
				.data("stn", ""+String.format("%05d",station.getStationID()))
				.data("yyyy", ""+time.getYear())
				.data("mm", ""+time.getMonthValue())
				.data("dd", ""+time.getDayOfMonth())
				.data("run", (time.getHour() == 0 ? "00": ""+time.getHour()))
				.get();
			
			//System.out.println("Document pre"+doc.select("pre").first().text());
			
			//doc = Jsoup.connect(urlBuilder).get();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		WeatherDataParserValidationResult result = parser.validate(doc.select("pre").first().text(), station);

		if (result.isValid()) {
			
			stations.add(result.getData().getStation());
			
			String datadir = ""+properties.get("data_dir");
			
			Path toSave = Path.of(datadir)
					.resolve(""+station.getArea().getAreaCode())
					.resolve(""+station.getStationID())
					.resolve(""+time.toLocalDate().format(DateTimeFormatter.ISO_DATE)+"T"+time.getHour()+".txt");
			try {
				
				Path p1 = Path.of(datadir).resolve(station.getArea().getAreaCode());
				if(!Files.exists(p1))
					Files.createDirectories(p1);
				Path p2 = p1.resolve(""+station.getStationID());
				if(!Files.exists(p2))
					Files.createDirectory(p2);
				
				Files.createFile(toSave);
				BufferedWriter writer = Files.newBufferedWriter(toSave);
				writer.write(doc.select("pre").first().text());
				writer.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			return result.getData();
		} else {
			System.out.println("No Valid Result from Parser");
			System.out.println(result.getException());
			// result.getException().printStackTrace();
			result.getData().setDateAndTime(time);
			return result.getData();
		}

	}

	@Override
	public Station buildStation(StationId stationID, String stationName, double longi, double lati, int elevation,
			String icao) {
		Station station = new Station(stationID, stationName, longi, lati, elevation, icao);
		return stations.stream().filter(arg -> arg.equals(station)).findFirst().orElse(station);
	}
}
