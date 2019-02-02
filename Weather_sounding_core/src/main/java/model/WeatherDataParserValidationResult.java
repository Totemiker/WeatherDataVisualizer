package model;

public class WeatherDataParserValidationResult 
{
	private boolean isValid = true;
	private Sounding data;
	private Exception exceptions;
	/**
	 * @return the isValid
	 */
	public boolean isValid() {
		return isValid;
	}
	/**
	 * @param isValid the isValid to set
	 */
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	/**
	 * @return the data
	 */
	public Sounding getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Sounding data) {
		this.data = data;
	}
	/**
	 * @return the exceptions
	 */
	public Exception getException() {
		return exceptions;
	}
	/**
	 * @param exceptions the exceptions to set
	 */
	public void setException(Exception exceptions) {
		this.exceptions = exceptions;
	}
}
