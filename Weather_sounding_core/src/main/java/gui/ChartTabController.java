/*
 * Copyright (c) 2019 Tobias Teumert
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import javafx.util.Pair;
import javafx.util.StringConverter;
/**
 * Controller of Chart Area
 * @author Tobias
 *
 */
public class ChartTabController {
	
	@FXML
	public LineChart<Number, Double> chartPane;
	
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
		((ValueAxis<Number>) chartPane.getXAxis()).setTickLength(1);
		((ValueAxis<Number>) chartPane.getXAxis()).setTickLabelRotation(-75D);
		
	//	((ValueAxis<Number>) chartPane.getXAxis()).setTickLabelFont();
	//	((ValueAxis<Number>) chartPane.getXAxis()).setMinorTickLength(1);
	//	((ValueAxis<Number>) chartPane.getXAxis()).setTickLabelsVisible(true);
	}
	
	/**
	 * Adding of a dataseries to the active chartpane
	 * @param createSeries the series to be added
	 */
	public void addSeries(Series<Number, Double> series) {
		chartPane.getData().add(series);

		List<Series<Number, Double>> listOfSeries = chartPane.getData();

		/**
		 * Browsing through the Data and applying ToolTip as well as the class on hover
		 */
		
		for (XYChart.Data<Number, Double> d : series.getData()) 
		{
			Tooltip t = new Tooltip(""+d.getYValue());
			t.setHideDelay(Duration.millis(100));
			t.setShowDelay(Duration.millis(100));
			Tooltip.install(d.getNode(),t);

			// Adding class on hover
			d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));

			// Removing class on exit
			d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
		}

		// long min;
		// long max;
		
		//max = bla.stream().mapToLong(serie -> serie.getData().stream().map(data -> data.getXValue().longValue()).reduce(Math::max).orElse(0L)).reduce(Math::max).orElse(0L);
				
		//min = bla.stream().mapToLong(serie -> serie.getData().stream().map(data -> data.getXValue().longValue()).reduce(Math::min).orElse(0L)).reduce(Math::min).orElse(0L);
		
		Pair<Long,Long> minMax = getLongPairFlatmap(listOfSeries);
		
		//bla.stream().map(this::getLongPair).reduce((a,b) -> new Pair<>(Math.min(a.getKey(),b.getKey()), Math.max(a.getValue(), b.getValue())));
		
				
		((ValueAxis<Number>) chartPane.getXAxis()).setLowerBound(minMax.getKey());
		((ValueAxis<Number>) chartPane.getXAxis()).setUpperBound(minMax.getValue());	
		
	}
			
	/*private Pair<Long,Long> getLongPair(Series<Number,Double> serie)
	{
		return serie.getData().stream()
			.map(data -> data.getXValue().longValue())
			.reduce(
					new Pair<Long,Long>(Long.MAX_VALUE,Long.MIN_VALUE),
					(pair, l) -> new Pair<Long,Long>(Math.min(pair.getKey(), l), Math.max(pair.getValue(), l)),
					(a,b) -> new Pair<>(Math.min(a.getKey(), b.getKey()), Math.max(a.getValue(), b.getValue())));		
		
	}*/
	/**
	 * Fetches every Data Pair from every Series and returns the Maximum and the Minimum value contained in serie as Pair
	 * @param serie List of Series
	 * @return Pair consisting of Max and Min value
	 */
	private Pair<Long,Long> getLongPairFlatmap(List<Series<Number,Double>> serie)
	{
		//fetches every Data Pair from every Series and returns the Maximum and the Minimum value contained in serie as Pair
		return
			serie.stream().flatMap(sers -> sers.getData().stream())
			.map(data -> data.getXValue().longValue())
			.reduce(new Pair<Long,Long>(Long.MAX_VALUE,Long.MIN_VALUE),
					(pair, l) -> new Pair<Long,Long>(Math.min(pair.getKey(), l), Math.max(pair.getValue(), l)),
					(a,b) -> new Pair<>(Math.min(a.getKey(), b.getKey()), Math.max(a.getValue(), b.getValue())));
	}

}
