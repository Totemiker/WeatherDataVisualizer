package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import control.ChartGUIController;
import control.DataProvider;
import control.LocalHddDataProvider;
import control.RamCacheDataProvider;
import control.WebDataProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application Window Class
 * Generates the Main-Window via FXML-Loader
 * @author Tobias
 *
 */
public class Main_GUI extends Application {
	
	final Properties properties = new Properties();
		
	@Override
	public void start(Stage primaryStage) 
	{
		loadProperties();
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ChartGUI.fxml"));

			Parent root = loader.load();
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					
			System.out.println("Properties: "+properties);
			
			//Setting of props into mainguicontroller
			
			//((ChartGUIController)loader.getController()).setProperties(properties);
			
			DataProvider provider = new RamCacheDataProvider(new LocalHddDataProvider(new WebDataProvider(properties),properties),properties);
			
			((ChartGUIController)loader.getController()).setDataProvider(provider);
			//((Main_GUI_Controller)loader.getController()).setData();
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("Weather Data Analyzer");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Overriden stop Method to do things in the stages of closing of Programm
	 */
	@Override
	public void stop(){
	    System.out.println("Stage is closing");
	    FileOutputStream out;
		
	    //Path path = Paths.get(System.getProperty("user.dir")+"\\ini.properties");
		//File file = Files.crea
	    
	    try {			
			out = new FileOutputStream(new File(System.getProperty("user.dir")+"\\ini.properties"));
			properties.store(out, "Properties from WDA");
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
	
			e.printStackTrace();
		}
	    
	    // Save file
	}
	/**
	 * Lädt die Properties von der Festplatte
	 * Wenn keine vorhanden sind werden Default werte erzeugt
	 */
	private void loadProperties()
	{
		// set defaults
		properties.setProperty("data_dir", (System.getProperty("user.dir")+"\\data"));
		//System.out.println(properties.get("data_dir"));
		properties.setProperty("weblink", "http://meteocentre.com/radiosonde/get_sounding.php?");
		
		// override values by loading from file
		try {
			properties.load(new FileInputStream(new File(System.getProperty("user.dir") + "\\ini.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
