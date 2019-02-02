package control;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Properties;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import model.Area;
import model.Sounding;
import model.Station;

public class Main_GUI_Controller 
{
	private Properties properties;
	private Data_Provider dataprovider;
	
	@FXML
	private ComboBox<Area> regionChooser;
	
	@FXML
	private ComboBox<Station> stationChooser;
	
	@FXML
	private Button selectButton;
	
	@FXML
	private ChoiceBox<LocalTime> startTimeChoiceBox, endTimeChoiceBox;
	
	@FXML
	private DatePicker datePickerStart, datePickerEnde;
	
	
	
	@FXML
	public void initialize()
	{
		regionChooser.setCellFactory(value -> new ListCell<>() {
			@Override
			protected void updateItem(Area item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.getAreaName());
				}
			};
		});
		
		regionChooser.setButtonCell(new ListCell<>() {
			@Override
			protected void updateItem(Area item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.getAreaName());
				}
			};
		});
		
		stationChooser.setCellFactory(value -> new ListCell<>() {
			@Override
			protected void updateItem(Station item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.getStationName());
				}
			};
		});
		
		stationChooser.setButtonCell(new ListCell<>() {
			@Override
			protected void updateItem(Station item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.getStationName());
				}
			};
		});
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	@FXML
	protected void chooseRegionAction(ActionEvent ae)
	{
		//System.out.println("Hier passiert was");
		//System.out.println(((ComboBox<Area>)ae.getSource()).getValue().getAreaName());
		stationChooser.getItems().clear();
		stationChooser.getItems().addAll(dataprovider.getStationsByArea(((ComboBox<Area>)ae.getSource()).getValue()));
	}
	
	@FXML
	protected void stationChooserAction(ActionEvent ae)
	{
		//maybe current station speichern
	}
	
	@FXML
	protected void selectButtonAction(ActionEvent ae)
	{
		LocalDate start,ende;
		LocalTime timeStart, timeEnde;
		
		System.out.println("Hier daten importieren");
		System.out.println("Start Date: "+datePickerStart.getValue());
		System.out.println("End Date: "+datePickerEnde.getValue());
		System.out.println("Time: "+startTimeChoiceBox.getValue());
		System.out.println("Time2: "+endTimeChoiceBox.getValue());
		
		//System.out.println(datePickerStart.getValue().isEqual(LocalDate.now()));
		
		if(regionChooser.getValue() == null || stationChooser.getValue() == null)
		{
			//TODO Fehlermeldung
			return;
		}
		
		if(datePickerEnde.getValue() == null)
		{
			if(datePickerStart.getValue() == null)
			{
				System.out.println("Kein valides Datum Start oder Ende");
				//Fehler
				return;
			}
			else
				datePickerEnde.setValue(datePickerStart.getValue());
		}
		
		if(endTimeChoiceBox.getValue() == null)
		{
			if(startTimeChoiceBox.getValue() == null)
			{
				System.out.println("Kein valides Datum Start oder Ende");
				return;
			}else
			{
				endTimeChoiceBox.setValue(startTimeChoiceBox.getValue());
			}
		}
		
		if(datePickerStart.getValue().isAfter(LocalDate.now()) 
				|| datePickerEnde.getValue().isAfter(LocalDate.now())
				|| (datePickerStart.getValue().isEqual(LocalDate.now()) && startTimeChoiceBox.getValue().isAfter(LocalTime.now().minusMinutes(90))
				|| (datePickerEnde.getValue().isEqual(LocalDate.now()) && endTimeChoiceBox.getValue().isAfter(LocalTime.now().minusMinutes(90)))))
		{
			//TODO korrekten Fehlerdialog designen
			System.out.println("Kein valides Datum Start oder Ende");
		}
		else
		{
			start = datePickerStart.getValue();
			ende = datePickerEnde.getValue();
			timeStart = startTimeChoiceBox.getValue();
			timeEnde = endTimeChoiceBox.getValue();
			
			LocalDateTime ldtstart = LocalDateTime.of(start, timeStart);
			LocalDateTime ldtende = LocalDateTime.of(ende, timeEnde);
			
			if(ldtstart.isEqual(ldtende))
			{
				System.out.println("Equal Timestamps");
				System.out.println("Station: "+stationChooser.getValue());
				Sounding temp = dataprovider.getSounding(stationChooser.getValue(), ldtstart, regionChooser.getValue());
				System.out.println("The Sounding: "+temp);
				//soundingsByStation.put(stationChooser.getValue(), temp);
			}
			else
			{
				
			}
		}
		
		
		
	}
	
	
	public void setData()
	{
		//regionChooser.getItems().addAll(dataprovider.getAreas());
	}
	
	public void setProperties(Properties properties)
	{
		this.properties = properties;
	}
	
	public void setDataProvider(Data_Provider provider)
	{
		this.dataprovider = provider;
		regionChooser.getItems().addAll(dataprovider.getAreas());
		startTimeChoiceBox.setItems(FXCollections.observableArrayList(LocalTime.of(0, 0), LocalTime.of(12, 0)));
		endTimeChoiceBox.setItems(FXCollections.observableArrayList(LocalTime.of(0, 0), LocalTime.of(12, 0)));
	}
}
