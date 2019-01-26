package model;

public class LevelData
{
	private String level;
	private double pressure;
	private double temp,dewPoint,direction,windspeed,height,theta,mix;
	
	public LevelData(String level, double pressure, double temp, double dewPoint, double direction, double windspeed,
			double height, double theta, double mix) {
		super();
		this.level = level;
		this.pressure = pressure;
		this.temp = temp;
		this.dewPoint = dewPoint;
		this.direction = direction;
		this.windspeed = windspeed;
		this.height = height;
		this.theta = theta;
		this.mix = mix;
	}
	
	/**
	 * 
	 * @param split
	 */
	public LevelData(String[] split) {
		
		level = split[0];
		pressure = Double.parseDouble(split[1]);
		temp = Double.parseDouble(split[2]);
		dewPoint = Double.parseDouble(split[3]);
		direction = Double.parseDouble(split[4]);
		windspeed = Double.parseDouble(split[5]);
		height = Double.parseDouble(split[6]);
		theta = Double.parseDouble(split[7]);
		mix = Double.parseDouble(split[8]);
		
	}


	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * @return the pressure
	 */
	public double getPressure() {
		return pressure;
	}

	/**
	 * @param pressure the pressure to set
	 */
	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	/**
	 * @return the temp
	 */
	public double getTemp() {
		return temp;
	}

	/**
	 * @param temp the temp to set
	 */
	public void setTemp(double temp) {
		this.temp = temp;
	}

	/**
	 * @return the dewPoint
	 */
	public double getDewPoint() {
		return dewPoint;
	}

	/**
	 * @param dewPoint the dewPoint to set
	 */
	public void setDewPoint(double dewPoint) {
		this.dewPoint = dewPoint;
	}

	/**
	 * @return the direction
	 */
	public double getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(double direction) {
		this.direction = direction;
	}

	/**
	 * @return the windspeed
	 */
	public double getWindspeed() {
		return windspeed;
	}

	/**
	 * @param windspeed the windspeed to set
	 */
	public void setWindspeed(double windspeed) {
		this.windspeed = windspeed;
	}

	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * @return the theta
	 */
	public double getTheta() {
		return theta;
	}

	/**
	 * @param theta the theta to set
	 */
	public void setTheta(double theta) {
		this.theta = theta;
	}

	/**
	 * @return the mix
	 */
	public double getMix() {
		return mix;
	}

	/**
	 * @param mix the mix to set
	 */
	public void setMix(double mix) {
		this.mix = mix;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LevelData [level=" + level + ", pressure=" + pressure + ", temp=" + temp + ", dewPoint=" + dewPoint
				+ ", direction=" + direction + ", windspeed=" + windspeed + ", height=" + height + ", theta=" + theta
				+ ", mix=" + mix + "]";
	}
	
}
