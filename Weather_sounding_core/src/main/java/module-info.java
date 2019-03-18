/**
 * 
 */
/**
 * @author Tobias
 *
 */
module weather_sounding_core {
	exports data.model;	
	exports bin;
	exports data.provider;
	exports gui;
	exports gui.util;

	requires com.google.common;
	requires transitive javafx.base;
	requires transitive javafx.controls;
	requires transitive javafx.fxml;
	requires transitive javafx.graphics;
	requires org.jsoup;
	
	opens gui to javafx.base,javafx.controls,javafx.fxml,javafx.graphics;
	
}