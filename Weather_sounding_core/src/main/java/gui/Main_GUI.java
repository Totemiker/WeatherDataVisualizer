/*
 * Copyright (c) 2019 Tobias Teumert
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import data.provider.DataProvider;
import data.provider.LocalHddDataProvider;
import data.provider.RamCacheDataProvider;
import data.provider.FixEmptyReadingsDataProvider;
import data.provider.WebDataProvider;
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
			System.out.println(getClass().getResource("ChartGUI.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ChartGUI.fxml"));

			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					
			//System.out.println("Properties: "+properties);
			
			//Creation of Data Provider
			
			WebDataProvider wdp = new WebDataProvider(properties);
			LocalHddDataProvider lhddp = new LocalHddDataProvider(wdp, properties);
			wdp.setRawCachingStrategy(lhddp::cacheRawSounding);
			RamCacheDataProvider ramcp = new RamCacheDataProvider(lhddp, properties);			
			
			DataProvider provider = new FixEmptyReadingsDataProvider(ramcp);
			
			((ChartGUIController)loader.getController()).setDataProvider(provider);		
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("Weather Data Analyzer");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Overriden stop Method to do complete Tasks in the stages of closing of the programm
	 */
	@Override
	public void stop(){
	    //System.out.println("Stage is closing");
	    		
	    //Path path = Paths.get(System.getProperty("user.dir")+"\\ini.properties");
		//File file = Files.crea
	    
	    try(FileOutputStream out = new FileOutputStream(new File(System.getProperty("user.dir")+"\\ini.properties"))) {			
			
			properties.store(out, "Properties from WDA");
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
	
			e.printStackTrace();
		}
	    
	    System.exit(0);
	    
	    // Save file
	}
	/**
	 * Loading of properties from HDD
	 * If no properties are present they are generated
	 */
	private void loadProperties()
	{
		// set defaults
		properties.setProperty("data_dir", (System.getProperty("user.dir")+"\\data"));
		
		System.out.println("User Directory "+System.getProperty("user.dir"));
		
		// set data source
		properties.setProperty("weblink", "http://meteocentre.com/radiosonde/get_sounding.php?");
		
		// override values by loading from file
		try {
			properties.load(new FileInputStream(new File(System.getProperty("user.dir") + "\\ini.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
