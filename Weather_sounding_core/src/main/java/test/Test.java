package test;

import java.time.LocalDate;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Test extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void start(Stage primaryStage)
	{
		primaryStage.setTitle("Hello World!");
		StackPane root = new StackPane();
		
		//LocalDateAxis xAxis = new LocalDateAxis();
		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();
		
		LineChart lineChart = new LineChart<Number, Number>(xAxis, yAxis);
		
		XYChart.Series<LocalDate, Number> series = new XYChart.Series();
        series.setName("My portfolio");
		
		series.getData().add(new XYChart.Data(12, 23D));
		series.getData().add(new XYChart.Data(15, 28D));
		
		lineChart.getData().add(series);
		
		root.getChildren().add(lineChart);
		
		primaryStage.setScene(new Scene(root, 300, 250));
		primaryStage.show();
	}
}