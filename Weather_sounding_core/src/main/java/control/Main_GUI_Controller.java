package control;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Area;
import model.LevelData;
import model.Sounding;
import model.Station;
import workspace.Worksheet;
import workspace.WorksheetController;

public class Main_GUI_Controller 
{
	private Properties properties;
		
	private WorksheetController controller;
		
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
	private TableView<Sounding> selectedSoundings;
	
	@FXML
	private TableColumn<Sounding, Integer> columnStationID, columnLevels;
	
	@FXML
	private TableColumn<Sounding, LocalDateTime> columnDate;
	
	@FXML
	private TableColumn<Sounding, LocalDateTime> columnTime;
	
	@FXML
	private ChoiceBox<Double> chooserLevelToPlot;
	
	@FXML
	private ChoiceBox<PlotCommand> chooserValueToPlot;

	
	
	@FXML
	public void initialize()
	{
		controller = new WorksheetController(new Worksheet());
		
		stationChooser.itemsProperty().bind(controller.getSheet().stationsProperty());
		regionChooser.itemsProperty().bind(controller.getSheet().areaProperty());
		
		startTimeChoiceBox.itemsProperty().bind(controller.getSheet().availableTimesProperty());
		endTimeChoiceBox.itemsProperty().bind(controller.getSheet().availableTimesProperty());
		
		datePickerStart.valueProperty().bindBidirectional(controller.getSheet().startDateProperty());
		datePickerEnde.valueProperty().bindBidirectional(controller.getSheet().endDateProperty());
		
		controller.getSheet().endTimeProperty().bind(endTimeChoiceBox.getSelectionModel().selectedItemProperty());
		controller.getSheet().startTimeProperty().bind(startTimeChoiceBox.getSelectionModel().selectedItemProperty());
		
		controller.getSheet().selectedAreaProperty().bind(regionChooser.valueProperty());
		controller.getSheet().selectedStationProperty().bind(stationChooser.valueProperty());
		
		selectButton.disableProperty().bind(Bindings.not(Bindings.createBooleanBinding(this::checkInputValidity, 
				stationChooser.getSelectionModel().selectedItemProperty(), 
				regionChooser.getSelectionModel().selectedItemProperty(),
				datePickerStart.valueProperty(),
				datePickerEnde.valueProperty(),
				startTimeChoiceBox.getSelectionModel().selectedItemProperty(),
				endTimeChoiceBox.getSelectionModel().selectedItemProperty())));
		
		selectedSoundings.itemsProperty().bind(controller.getSheet().selectedSoundingsProperty());
		
		//TODO Bindings
		
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
		
		////TABLE
	
		columnStationID.setCellValueFactory(new PropertyValueFactory<>("stationID"));
		columnDate.setCellValueFactory(arg -> new SimpleObjectProperty<LocalDateTime>(arg.getValue().getDateAndTime()));
		columnLevels.setCellValueFactory(new PropertyValueFactory<>("levels"));
		columnTime.setCellValueFactory(arg -> new SimpleObjectProperty<LocalDateTime>(arg.getValue().getDateAndTime()));
		
		columnDate.setCellFactory(arg -> new TableCell<Sounding, LocalDateTime>() {
		    @Override
		    protected void updateItem(LocalDateTime item, boolean empty) {

		        super.updateItem(item, empty);
		        if (empty)
		            setText(null);
		        else
		            setText(""+item.toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.GERMANY)));
		    }
		});
		
		columnTime.setCellFactory(arg -> new TableCell<Sounding, LocalDateTime>() {
		    @Override
		    protected void updateItem(LocalDateTime item, boolean empty) {

		        super.updateItem(item, empty);
		        if (empty)
		            setText(null);
		        else
		            setText(""+item.toLocalTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).withLocale(Locale.GERMANY)));
		    }
		});
		
	}
	
	
	@SuppressWarnings("unchecked")
	@FXML
	protected void chooseRegionAction(ActionEvent ae)
	{
		controller.switchRegionTo(((ComboBox<Area>)ae.getSource()).getValue());
	}
	
	@FXML
	protected void stationChooserAction(ActionEvent ae)
	{
		//maybe current station speichern
	}
	/**
	 * Autoaufruf bei Buttonclick "Select & Clear
	 * @param ae Das Zugehörige ActionEvent
	 */
	@FXML
	protected void selectButtonAction(ActionEvent ae)
	{
		System.out.println("Clear & Select GUI");
		controller.clearAndSelect();						
	}
	
	@FXML
	protected void addSelectButtonAction(ActionEvent ae)
	{
		System.out.println("Add to selection");
		controller.addToSelection();
		
	}
	
	private boolean checkInputValidity()
	{		
		// FIXME check edge cases
		boolean isValid = true;
		
		isValid &= !(regionChooser.getValue() == null);
		isValid &= !(stationChooser.getValue() == null);
		isValid &= !(datePickerEnde.getValue() == null);
		isValid &= !(datePickerStart.getValue() == null);
		isValid &= !(endTimeChoiceBox.getValue() == null);
		isValid &= !(startTimeChoiceBox.getValue() == null);
		
		if (isValid) {				
			isValid &= !(datePickerStart.getValue().isAfter(LocalDate.now()));
			isValid &= !(datePickerEnde.getValue().isAfter(LocalDate.now()));
			isValid &= !(datePickerStart.getValue().isAfter(datePickerEnde.getValue()));
			isValid &= !(datePickerStart.getValue().isEqual(datePickerEnde.getValue()) && startTimeChoiceBox.getValue().isAfter(endTimeChoiceBox.getValue()));
		}
		return isValid;
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
		controller.setProvider(provider);				
		populateValueChooser();
	}
	
	private void populateValueChooser()
	{
		List<PlotCommand> values = new ArrayList<PlotCommand>();
		
		values.add(new PlotCommand("Temperature", LevelData::getTemp));
		values.add(new PlotCommand("Dew Point", LevelData::getDewPoint));
		values.add(new PlotCommand("Wind Direction", LevelData::getDirection));
		values.add(new PlotCommand("Wind Speed", LevelData::getWindspeed));
		values.add(new PlotCommand("Height", LevelData::getHeight));
		values.add(new PlotCommand("Theta", LevelData::getTheta));
		values.add(new PlotCommand("Mix", LevelData::getMix));
		chooserValueToPlot.getItems().addAll(FXCollections.observableArrayList(values));
	}
}
