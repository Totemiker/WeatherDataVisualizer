package model;

import java.time.LocalDateTime;

import model.Reading.LevelType;

/**
 * Constructs a Key to a Reading stored in the Cache
 * @author Tobias
 *
 */
public class ReadingKey 
{
	StationId id;
	LocalDateTime date;
	LevelType type;
	
	public ReadingKey(StationId id, LocalDateTime ldt, LevelType type) {
		this.id = id;
		this.date = ldt;
		this.type = type;
	}
	
	public StationId getId() {
		return id;
	}
	
	public LevelType getType() {
		return type;
	}
	
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
	