package workspace;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart.Series;
import model.LevelData;
/**
 * Hält die Arbeitsdaten vor (Viewmodel) 
 * @author Tobias
 *
 */
public class Worksheet {	
	
	/** Geladene Rohdaten*/
	private final ObjectProperty<ObservableList<LevelData>> selectedValues;
	
	/** Geplottete Daten*/
	private final ObjectProperty<ObservableList<Series<String, Double>>> dataToChart;
	
	
	
	public Worksheet() {
		
		dataToChart		 = new SimpleObjectProperty<>(this, "chartedData", FXCollections.observableArrayList());
		selectedValues	 = new SimpleObjectProperty<>(this, "selectedLevelData", FXCollections.observableArrayList());		
	}
	
	public Property<ObservableList<Series<String,Double>>> dataToChartProperty()
	{
		return dataToChart;
	}
	
	public void setDataToChart(List<Series<String,Double>> data)
	{
		if(data instanceof ObservableList<?>)
			dataToChart.set((ObservableList<Series<String,Double>>)data);
		else
			dataToChart.set(FXCollections.observableArrayList(data));
	}
	
	public ObservableList<Series<String,Double>> getDataToChart()
	{
		return dataToChart.get();
	}
	
	public Property<ObservableList<LevelData>> selectedValuesProperty()
	{
		return selectedValues;
	}
	
	public void setSelectedValues(List<LevelData> data)
	{
		if(data instanceof ObservableList<?>)
			selectedValues.set((ObservableList<LevelData>) data);
		else
			selectedValues.set(FXCollections.observableArrayList(data));
	}
	
	public ObservableList<LevelData> getSelectedValues()
	{
		return selectedValues.get();
	}
	
	
}
