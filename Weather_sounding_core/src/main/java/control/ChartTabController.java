package control;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Series;

public class ChartTabController {
	
	@FXML
	LineChart<String, Double> chartPane;

	public void addSeries(Series<String, Double> createSeries)
	{
		chartPane.getData().add(createSeries);		
	}

}
