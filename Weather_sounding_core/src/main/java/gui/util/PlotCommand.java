package gui.util;

import java.util.function.Function;

import data.model.Reading;

/**
 * Class to extract a value as double from a given Reading by supplied extractor function
 * @author Tobias
 *
 */
public class PlotCommand {
	
	private String displayName;
	/** The function to be applied*/
	Function<Reading, Double> extractor;
	
	public PlotCommand(String displayName, Function<Reading, Double> extractor) 
	{
		this.setDisplayName(displayName);
		this.extractor = extractor;
	}
	
	/**
	 * Extracts the double-value from the given Reading by executing the supplied extractor Function
	 * @param data	The Reading to extract data from
	 * @return		The corresponding double value
	 */
	public double execute(Reading data)
	{
		return extractor.apply(data);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
