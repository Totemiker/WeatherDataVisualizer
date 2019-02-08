package data.provider;

public abstract class ChainedDataProvider extends AbstractDataProvider {
	
	protected DataProvider upstream;
}
