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
package data.model;

/**
 * One record / Measurement (at a specific station, taken at a specific pressure) for multiple Parameters<p>
 * Parameters are Temperature, Pressure, Dewpoint, Winddirection, Windspeed, Height, Theta and Mix<p> 
 * level = type of record, not pressure!
 * @author Tobias
 *
 */
public class Reading implements Cloneable
{
	/**
	 * Enum class to link a specific pressure value to a shorthand description of the level-type<p>
	 * Some entrys have unspecified pressure levels because they have different pressure values in multiple Readings
	 * @author Tobias
	 *
	 */
	public static enum LevelType {
		TRO1(-1),
		TRO2(-1),
		SFC(-1),
		MAND_1000(1000),
		MAND_925(925),
		MAND_850(850),
		MAND_700(700),
		MAND_500(500),
		MAND_400(400),
		MAND_300(300),
		MAND_250(250),
		MAND_200(200),
		MAND_150(150),
		MAND_100(100),
		MAND_70(70),
		MAND_50(50),
		MAND_30(30),
		MAND_20(20),
		MAND_10(10),
		MAXW(-1),
		CUSTOM(-1);
		
		private final double pressure;
		/**
		 * Constructor for enum type
		 * @param pressure the given pressure
		 */
		private LevelType(double pressure) {
			this.pressure = pressure;
		}
		/**
		 * Getter for the assossiated pressure value
		 * @return
		 */
		public double getPressure() {
			return pressure;
		}
	};
	
	private LevelType level;
	private double pressure;
	private double temp,dewPoint,direction,windspeed,height,theta,mix;
	
	public Reading() {
		
	}
	/**
	 * Standard Reading constructor to construct a Reading
	 * @param level The Level of the Reading as enum Type
	 * @param pressure The Pressure of the Reading in mbar
	 * @param temp The absolute Temperature in centigrade of the Reading
	 * @param dewPoint The Dewpoint Temperature of the Reading in centigrade
	 * @param direction The direction of the wind in degree
	 * @param windspeed The speed of wind in kts
	 * @param height The Height of the reading in meter
	 * @param theta the Theta Value
	 * @param mix The Mix Value
	 */
	public Reading(LevelType level, double pressure, double temp, double dewPoint, double direction, double windspeed,
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
	 * Creates a Reading from a preparsed and presorted Array with a given Leveltype
	 * @param split All Measurements as Array presorted and preparsed (No invalid values)
	 * @param level The LevelType to the corresponding Reading
	 */
	public Reading(String[] split, LevelType level) {
		
		this.level = level;
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
	 * Getter for Leveltype
	 * @return the Leveltype
	 */
	public LevelType getLevel() {
		return level;
	}

	/**
	 * Setter for Leveltype
	 * @param level the level to set
	 */
	public void setLevel(LevelType level) {
		this.level = level;
	}

	/**
	 * Getter for Pressure
	 * @return the pressure
	 */
	public double getPressure() {
		return pressure;
	}

	/**
	 * Setter for Pressure
	 * @param pressure the pressure to set
	 */
	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	/**
	 * Getter for Temperature
	 * @return the temp
	 */
	public double getTemp() {
		return temp;
	}

	/**
	 * Setter for Temperature
	 * @param temp the temp to set
	 */
	public void setTemp(double temp) {
		this.temp = temp;
	}

	/**
	 * Getter for Dewpoint
	 * @return the dewPoint
	 */
	public double getDewPoint() {
		return dewPoint;
	}

	/**
	 * Setter for Dewpoint
	 * @param dewPoint the dewPoint to set
	 */
	public void setDewPoint(double dewPoint) {
		this.dewPoint = dewPoint;
	}

	/**
	 * Getter for Direction
	 * @return the direction
	 */
	public double getDirection() {
		return direction;
	}

	/**
	 * Setter for Direction
	 * @param direction the direction to set
	 */
	public void setDirection(double direction) {
		this.direction = direction;
	}

	/**
	 * Getter for the Windspeed value
	 * @return the windspeed
	 */
	public double getWindspeed() {
		return windspeed;
	}

	/**
	 * Setter for the windspeed value
	 * @param windspeed the windspeed to set
	 */
	public void setWindspeed(double windspeed) {
		this.windspeed = windspeed;
	}

	/**
	 * getter for the height value
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Setter for the Height value
	 * @param height the height to set
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * Getter for the Theta value
	 * @return the theta
	 */
	public double getTheta() {
		return theta;
	}

	/**
	 * Setter for the Theta value
	 * @param theta the theta value to set
	 */
	public void setTheta(double theta) {
		this.theta = theta;
	}

	/**
	 * Getter for the Mix value
	 * @return the mix
	 */
	public double getMix() {
		return mix;
	}

	/**
	 * Setter for The Mix Parameter
	 * @param mix the mix value to set
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

	/*
	 * (non-Javadoc) 
	 * @see java.lang.Object#clone()
	 */
	public Reading clone() {
		try {
			return (Reading) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return new Reading();
	}
	
}
