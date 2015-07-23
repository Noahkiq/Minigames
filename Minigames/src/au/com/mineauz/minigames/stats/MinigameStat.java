package au.com.mineauz.minigames.stats;

public abstract class MinigameStat {
	private final String name;
	private String displayName;
	private final StatFormat format;
	
	MinigameStat(String name, StatFormat format) {
		this.name = name;
		this.format = format;
	}
	
	/**
	 * @return Returns the name of this stat
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the general format of this stat.
	 */
	public StatFormat getFormat() {
		return format;
	}
	
	/**
	 * @return Returns the current name for display purposes. 
	 * This will be the display name if set, otherwise it will be the actual name
	 */
	public String getDisplayName() {
		if (displayName != null) {
			return displayName;
		} else {
			return name;
		}
	}
	
	/**
	 * Sets the name that is displayed for this stat.
	 * @param name The new name or null to reset
	 */
	public void setDisplayName(String name) {
		displayName = name;
	}
	
	/**
	 * @return Returns true if the format of this stat can be overridden per minigame
	 */
	public abstract boolean canOverrideFormat();
	
	/**
	 * Queries this stat to check if its value should actually be stored.
	 * The normal behaviour is to always save the stat unless the value 
	 * is 0 and the format doesn't contain the minimum or last value  
	 * @param value The value of this stat
	 * @param actualFormat The format being saved. This will differ from the
	 *        format returned by {@link #getFormat()} when overridden by
	 *        specific minigames
	 * @return True if the value should be saved
	 */
	public boolean shouldStoreStat(long value, StatFormat actualFormat) {
		if (value == 0) {
			switch (actualFormat) {
			case Last:
			case LastAndTotal:
			case Min:
			case MinAndTotal:
			case MinMax:
			case MinMaxAndTotal:
				return true;
			default:
				return false;
			}
		} else {
			return true;
		}
	}
}
