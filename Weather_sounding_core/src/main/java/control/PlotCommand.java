package control;

import java.util.function.Function;

import model.LevelData;

public class PlotCommand {
	
	private String displayName;
	
	Function<LevelData, Double> extractor;
	
	public PlotCommand(String displayName, Function<LevelData, Double> extractor) 
	{
		this.setDisplayName(displayName);
		this.extractor = extractor;
	}
	
	public double execute(LevelData data)
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
