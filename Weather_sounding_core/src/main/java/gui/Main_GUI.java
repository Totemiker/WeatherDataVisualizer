package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import control.ChartGUIController;
import control.Data_Provider;
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
	
	final Properties properties = new Properties(/*DEFAULT_PROPERTIES*/);
	/*public static final Properties DEFAULT_PROPERTIES = new Properties(); 
	
	static {
		DEFAULT_PROPERTIES.setProperty("beispiel", "wert");
	}*/
	
	@Override
	public void start(Stage primaryStage) 
	{
		loadProperties();
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ChartGUI.fxml"));

			Parent root = loader.load();
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// System.out.println("MAIN: "+scene.getStylesheets());
			// primaryStage.setMaximized(true);
			
			System.out.println("Properties: "+properties);
			
			//Setting of props into mainguicontroller
			
			//((ChartGUIController)loader.getController()).setProperties(properties);
			((ChartGUIController)loader.getController()).setDataProvider(new Data_Provider(properties));
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		properties.setProperty("weblink", "http://meteocentre.com/radiosonde/get_sounding.php?");
		
		// override values by loading from file
		try {
			properties.load(new FileInputStream(new File(System.getProperty("user.dir") + "\\ini.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
