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

import java.time.LocalDateTime;

import data.model.Reading.LevelType;

/**
 * Constructs a Key to a Reading stored in the Cache
 * @author Tobias
 *
 */
public class ReadingKey 
{
	/**
	 * The StationID
	 */
	private StationId id;
	/**
	 * The Date of the reading
	 */
	private LocalDateTime date;
	/**
	 * The Leveltype of the Reading
	 */
	private LevelType type;
	
	/**
	 * Constructs a Reading Key from given values
	 * @param id	The StationID
	 * @param ldt	The Date
	 * @param type	The Leveltype
	 */
	public ReadingKey(StationId id, LocalDateTime ldt, LevelType type) {
		this.id = id;
		this.date = ldt;
		this.type = type;
	}
	
	/**
	 * Getter for Station ID
	 * @return the StationID
	 */
	public StationId getId() {
		return id;
	}
	
	/**
	 * Getter for Leveltype
	 * @return the Leveltype
	 */
	public LevelType getType() {
		return type;
	}
	/**
	 * Getter for the Date
	 * @return The Date
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReadingKey other = (ReadingKey) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	
}
	