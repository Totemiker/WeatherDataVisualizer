package control;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import model.LevelData;

public class PlotCommand {
	
	String displayName;
	
	Function<LevelData, Double> extractor;
	
	public PlotCommand(String displayName, Function<LevelData, Double> extractor) 
	{
		this.displayName = displayName;
		this.extractor = extractor;
	}
	
	public double execute(LevelData data)
	{
		return extractor.apply(data);
	}
	
	public static void main(String[] args) {
		List<PlotCommand> commands = new ArrayList<>();
		
		commands.add(new PlotCommand("Temperature", LevelData::getTemp));
		
	}

}
