package data.model;

public class WeatherDataParserValidationResult 
{
	private boolean isValid = true;
	private Sounding data;
	private Exception exceptions;
	/**
	 * Validity of parsed data
	 * @return the isValid
	 */
	public boolean isValid() {
		return isValid;
	}
	/**
	 * Setter for the validity
	 * @param isValid the isValid to set
	 */
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	/**
	 * Getter for the Sounding
	 * @return the parsed data
	 */
	public Sounding getData() {
		return data;
	}
	/**
	 * Setter for Sounding
	 * @param data the data to set
	 */
	public void setData(Sounding data) {
		this.data = data;
	}
	/**
	 * Getter for associated exception
	 * @return the exception
	 */
	public Exception getException() {
		return exceptions;
	}
	/**
	 * setter for the exception
	 * @param exceptions the exceptions to set
	 */
	public void setException(Exception exceptions) {
		this.exceptions = exceptions;
	}
}
