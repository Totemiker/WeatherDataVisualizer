package control;

import java.time.LocalDate;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import model.Area;
import model.LevelData.LevelType;
import model.Station;
import workspace.Worksheet;
import workspace.WorksheetController;

/**
 * Controller Klasse der neuen Oberfläche
 * @author Tobias
 *
 */
public class ChartGUIController {
	
	private WorksheetController controller;
	
	/**
	 * Shows the available Areas to the user
	 * Holds the chosen Area
	 */
	@FXML
    private ComboBox<Area> choiceRegion;
	
	/**
	 * Shows the available Stations of the chosen Area
	 * Holds the chosen Station
	 */
    @FXML
    private ComboBox<Station> choiceStation;
    
    /**
     * Startdate of timeinterval from which data is to be pulled
     */
    @FXML
    private DatePicker pickerStartDate;
    
    /**
     * Enddate of timeinterval from which data is to be pulled
     */
    @FXML
    private DatePicker pickerEndDate;
    
    /**
     * Checks wether 12 o'clock data or midnight data should be pulled
     */
    @FXML
    private CheckBox checkBoxTime;
    
    /**
     * Color of the corresponding Series in the Chart
     */
    @FXML
    private ColorPicker colorPickerSeriesColor;
    
    /**
     * optional Name for Series
     */
    @FXML
    private TextField textfieldSeriesName;
    
    /**
     * Which Value should be plotted
     */
    @FXML
    private ComboBox<PlotCommand> choiceValue;
    
    /**
     * which level should be plotted
     */
    @FXML
    private ComboBox<LevelType> choiceLevel;
    
    /**
     * Creates the Chart to Display in the active Chart tab
     */
    @FXML
    private Button buttonAddSeries;
    
	/**
	 * Autocalled Method - initialized by FXMLLoader
	 */
	@FXML
	public void initialize()
	{
		//TODO Bindings CellFactorys
		
		controller = new WorksheetController(new Worksheet());
		
		choiceStation.itemsProperty().bind(controller.stationsProperty());
		choiceRegion.itemsProperty().bind(controller.areaProperty());
		choiceLevel.itemsProperty().bind(controller.plottableLevelsProperty());
		choiceValue.itemsProperty().bind(controller.plottableValuesProperty());
		
		controller.levelToPlotProperty().bind(choiceLevel.valueProperty());
		controller.valueToPlotProperty().bind(choiceValue.valueProperty());
		
		pickerStartDate.valueProperty().bindBidirectional(controller.startDateProperty());
		pickerEndDate.valueProperty().bindBidirectional(controller.endDateProperty());
		checkBoxTime.selectedProperty().bindBidirectional(controller.timeProperty());
		colorPickerSeriesColor.valueProperty().bindBidirectional(controller.pickedColourProperty());
		
		buttonAddSeries.disableProperty().bind(Bindings.not(Bindings.createBooleanBinding(this::checkInputValidity,
				choiceStation.getSelectionModel().selectedItemProperty(), 
				choiceRegion.getSelectionModel().selectedItemProperty(),
				pickerStartDate.valueProperty(),
				pickerEndDate.valueProperty(),
				choiceLevel.getSelectionModel().selectedItemProperty(),
				choiceValue.getSelectionModel().selectedItemProperty(),
				colorPickerSeriesColor.valueProperty())));
		
		/////////////////
		
		initializeDataPresentation();
		
	}
	
		
	/**
	 * Initialises all GUI-Elements with the appropriate data Factorys
	 */
	private void initializeDataPresentation()
	{
		choiceRegion.setCellFactory(value -> new ListCell<>() {
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
		
		choiceRegion.setButtonCell(new ListCell<>() {
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
		
		choiceStation.setCellFactory(value -> new ListCell<>() {
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
		
		choiceStation.setButtonCell(new ListCell<>() {
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
		
		choiceLevel.setCellFactory(value -> new ListCell<>() {
			@Override
			protected void updateItem(LevelType item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.toString());
				}
			};
		});
		
		choiceLevel.setButtonCell(new ListCell<>() {
			@Override
			protected void updateItem(LevelType item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.toString());
				}
			};
		});
		
		
		choiceValue.setCellFactory(value -> new ListCell<>() {
			@Override
			protected void updateItem(PlotCommand item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.getDisplayName());
				}
			};
		});
		
		choiceValue.setButtonCell(new ListCell<>() {
			@Override
			protected void updateItem(PlotCommand item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.getDisplayName());
				}
			};
		});
	}
	
	/**
	 * Sets the Dataprovider of the Program
	 * @param provider The Dataprovider from where to get Data
	 */
	public void setDataProvider(Data_Provider provider)
	{		
		controller.setProvider(provider);		
	}
	
	 @FXML
	protected void buttonActionAddSeries(ActionEvent event) {
		 //TODO Korrekter Aufbau des Charts
	 }
	
	 @FXML
	protected void choiceActionRegion(ActionEvent event)
	{
		 controller.switchRegionTo(((ComboBox<Area>)event.getSource()).getValue());
	}
	
	private boolean checkInputValidity()
	{		
		// FIXME check edge cases
		boolean isValid = true;
		
		isValid &= !(choiceRegion.getValue() == null);
		isValid &= !(choiceStation.getValue() == null);
		isValid &= !(choiceLevel.getValue() == null);
		isValid &= !(choiceValue.getValue() == null);
		isValid &= !(pickerStartDate.getValue() == null);
		isValid &= !(pickerEndDate.getValue() == null);
		isValid &= !(colorPickerSeriesColor.getValue() == null);
		//System.out.println(isValid);
		
		/*if (isValid) {				
			isValid &= !(pickerStartDate.getValue().isAfter(LocalDate.now()));
			isValid &= !(pickerEndDate.getValue().isAfter(LocalDate.now()));
			isValid &= !(pickerStartDate.getValue().isAfter(pickerEndDate.getValue()));			
			//isValid &= !(datePickerStart.getValue().isAfter(datePickerEnde.getValue()));
									
			/*isValid &= !(datePickerStart.getValue().isEqual(datePickerEnde.getValue())&&  //true
					(startTimeChoiceBox.getValue().isAfter(LocalTime.now()) || 			//true
					 startTimeChoiceBox.getValue().isAfter(endTimeChoiceBox.getValue()) ||
					 endTimeChoiceBox.getValue().isAfter(LocalTime.now()) ||
					 	(startTimeChoiceBox.getValue().isAfter(LocalTime.now())) &&
					 	 endTimeChoiceBox.getValue().isAfter(LocalTime.now())));
			
		}*/
		return isValid;
	}
			

}
