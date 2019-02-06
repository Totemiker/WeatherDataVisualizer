package control;

import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import model.Area;
import model.Reading.LevelType;
import model.StationId;
import workspace.Worksheet;
import workspace.WorksheetController;

/**
 * Controller Klasse der neuen Oberfläche
 * @author Tobias
 *
 */
public class ChartGUIController {
	
	private WorksheetController controller;
	
	@FXML
    private MenuItem menuNewChart;
	
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
    private ComboBox<StationId> choiceStation;
    
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
     * Creates a new Series to Display in the active Chart tab
     */
    @FXML
    private Button buttonAddSeries;
    
    /**
     * The Tab Pane that holds multiple Series. Can have Multiple Charts each in a new Tab new Tab via Menu -> Loads from FXML
     */
    @FXML
    private TabPane tabPaneCharts;
    
	/**
	 * Autocalled Method - initialized by FXMLLoader
	 */
	@FXML
	public void initialize()
	{
		//TODO Bindings CellFactorys
		
		controller = new WorksheetController(new Worksheet());
		
		choiceStation.itemsProperty().bind(controller.stationIdsProperty());
		choiceRegion.itemsProperty().bind(controller.areaProperty());
		choiceLevel.itemsProperty().bind(controller.plottableLevelsProperty());
		choiceValue.itemsProperty().bind(controller.plottableValuesProperty());
		
		controller.levelToPlotProperty().bind(choiceLevel.valueProperty());
		controller.valueToPlotProperty().bind(choiceValue.valueProperty());
		controller.selectedStationIdProperty().bind(choiceStation.valueProperty());

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
			protected void updateItem(StationId item, boolean empty) {
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
			protected void updateItem(StationId item, boolean empty) {
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
	public void setDataProvider(DataProvider provider)
	{		
		controller.setProvider(provider);		
	}
	
	@FXML
	protected void buttonActionAddSeries(ActionEvent event) 
	{
		 //TODO Korrekter Aufbau des Charts
		System.out.println("Add Series to active Chart");
		controller.addSeries();
	}
	
	/**
	 * Repopulates the Stationlist when Region is switched
	 * @param event The thrown ActionEvent from the GUI
	 */
	@FXML
	protected void choiceActionRegion(ActionEvent event)
	{
		 controller.switchRegionTo(((ComboBox<Area>)event.getSource()).getValue());
	}
	
	/**
	 * Adds a new Tab with a new Chart to the TabPane tabPaneCharts
	 * @param event The thrown ActionEvent from the GUI
	 */
	@FXML
	protected void menuActionNewChart(ActionEvent event) 
	{
		 System.out.println("New Chart");
		 try {
			//FXMLLoader.load(getClass().getResource("ChartTab.fxml"));
			Tab newTab = new Tab("Chart " + (tabPaneCharts.getTabs().size()+1));
			newTab.setContent(FXMLLoader.load(getClass().getResource("/gui/ChartTab.fxml")));
			tabPaneCharts.getTabs().add(newTab);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	/**
	 * Checks wether all conditions of adding a new Series are fullfilled
	 * @return validity of adding a new Series
	 */
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
