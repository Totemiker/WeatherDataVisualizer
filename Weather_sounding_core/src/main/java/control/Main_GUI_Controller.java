package control;

import java.util.Properties;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class Main_GUI_Controller 
{
	private Properties properties;
	private Data_Provider dataprovider;
	
	@FXML
	private ComboBox<String> region_Chooser;
	
	@FXML
	ComboBox<Integer> station_Chooser;
		
	
	public void setData()
	{
		
	}
	
	public void setProperties(Properties properties)
	{
		this.properties = properties;
	}
	
	public void setDataProvider(Data_Provider provider)
	{
		this.dataprovider = provider;
	}
}
