/**
 * 
 */
package discovery.environment;

/**
 * @author James Giller 109466711
 */
public class TimeOfDay {

	private static String[] DAYS = {"MON", "TUE",
		                            "WED", "THU",
		                            "FRI", "SAT",
		                            "SUN"};
	/**
	 * The day of the week.
	 */
	private final int day;
	/**
	 * The hour of the day.
	 */
	private final int hour;
	/**
	 * The minute of the hour.
	 */
	private final int minute;
	
	/**
	 * @param day
	 * @param hour
	 * @param minute
	 */
	public TimeOfDay(int day, int hour, int minute) {
		this.day = day;
		this.hour = hour;
		this.minute = minute;
	}

	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @return the hour
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * @return the minute
	 */
	public int getMinute() {
		return minute;
	}
	
	@Override
	public String toString() {
		return String.format( "%s %02d:%02d", DAYS[day], hour, minute );
	}
}
