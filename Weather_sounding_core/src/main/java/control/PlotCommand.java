package control;

import java.util.function.Function;

import model.Reading;

public class PlotCommand {
	
	private String displayName;
	
	Function<Reading, Double> extractor;
	
	public PlotCommand(String displayName, Function<Reading, Double> extractor) 
	{
		this.setDisplayName(displayName);
		this.extractor = extractor;
	}
	
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
