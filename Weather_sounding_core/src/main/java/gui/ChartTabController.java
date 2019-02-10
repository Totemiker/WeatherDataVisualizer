package gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.util.Pair;
import javafx.util.StringConverter;

public class ChartTabController {
	
	@FXML
	LineChart<Number, Double> chartPane;
	
	//private final Set<String> cat = new LinkedHashSet<>();
	//LocalDateTimeAxis test;
	
	//CategoryAxis;	
	//LocalDateTime ldt = LocalDateTime.now();
	
	//TODO Implementation of x-Axis Class with LocalDate
	
	@FXML
	public void initialize()
	{
		//chartPane.getXAxis()
		((ValueAxis<Number>) chartPane.getXAxis()).setTickLabelFormatter(new StringConverter<Number>() {
			
			@Override
			public String toString(Number l) {
				//long temp = l.longValue();
				LocalDate ldt = LocalDate.ofEpochDay(l.longValue());
				return ldt.format(DateTimeFormatter.ISO_DATE); // 2019-01-01 ...
			}
			
			@Override
			public Long fromString(String string) {
				// 2019-01-01
				System.out.println("IN FROM STRING");
				
				return Long.parseLong(string);
			}
		});
		((ValueAxis<Number>) chartPane.getXAxis()).setAutoRanging(false);
	}
	
	/**
	 * Setzt der chartpane in die dataproperty die anzuzeigenden Daten
	 * @param createSeries Die anzuzeigende Serie
	 */
	public void addSeries(Series<Number, Double> series)
	{
		chartPane.getData().add(series);
				
		List<Series<Number,Double>> bla = chartPane.getData();
		
		//long min;
		//long max;
		
		//max = bla.stream().mapToLong(serie -> serie.getData().stream().map(data -> data.getXValue().longValue()).reduce(Math::max).orElse(0L)).reduce(Math::max).orElse(0L);
				
		//min = bla.stream().mapToLong(serie -> serie.getData().stream().map(data -> data.getXValue().longValue()).reduce(Math::min).orElse(0L)).reduce(Math::min).orElse(0L);
		
		Pair<Long,Long> minMax = getLongPairFlatmap(bla);
		
		//bla.stream().map(this::getLongPair).reduce((a,b) -> new Pair<>(Math.min(a.getKey(),b.getKey()), Math.max(a.getValue(), b.getValue())));
		
				
		((ValueAxis<Number>) chartPane.getXAxis()).setLowerBound(minMax.getKey());
		((ValueAxis<Number>) chartPane.getXAxis()).setUpperBound(minMax.getValue());	
		
	}
			
	private Pair<Long,Long> getLongPair(Series<Number,Double> serie)
	{
		return serie.getData().stream()
			.map(data -> data.getXValue().longValue())
			.reduce(
					new Pair<Long,Long>(Long.MAX_VALUE,Long.MIN_VALUE),
					(pair, l) -> new Pair<Long,Long>(Math.min(pair.getKey(), l), Math.max(pair.getValue(), l)),
					(a,b) -> new Pair<>(Math.min(a.getKey(), b.getKey()), Math.max(a.getValue(), b.getValue())));
		
		
	}
	
	private Pair<Long,Long> getLongPairFlatmap(List<Series<Number,Double>> serie)
	{
		return
			serie.stream().flatMap(sers -> sers.getData().stream())
			.map(data -> data.getXValue().longValue())
			.reduce(new Pair<Long,Long>(Long.MAX_VALUE,Long.MIN_VALUE),
					(pair, l) -> new Pair<Long,Long>(Math.min(pair.getKey(), l), Math.max(pair.getValue(), l)),
					(a,b) -> new Pair<>(Math.min(a.getKey(), b.getKey()), Math.max(a.getValue(), b.getValue())));
	}

}
