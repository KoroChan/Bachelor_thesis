package discovery.environment;

public class StateFactory {
    
	private static final double RUSH_HOUR = 0.15;
	private static final double MEDIUM = 0.07;
	private static final double QUIET = 0.04;
	
	// SIMPLE PATTERNS
	
	public static ContactAreaState hourlyPattern() {
		ContactAreaState state = new ContactAreaState( 10 );
		for ( int day = 0; day < 10; day++ ) {
			for ( int hour = 0; hour < 24; hour++ ) {
				state.simulateActivity( day, hour, 25, 5, 1 );
			}
		}
		return state;
	}
	
	public static ContactAreaState dailyPattern() {
		ContactAreaState state = new ContactAreaState( 10 );
		for ( int day = 0; day < 10; day++ ) {
			state.simulateActivity( day, 8, 55, 5, MEDIUM );
		    state.simulateActivity( day, 9, 0, 5, RUSH_HOUR );
		    state.simulateActivity( day, 9, 5, 5, QUIET );
		    
		    state.simulateActivity( day, 12, 55, 5, RUSH_HOUR );
		    state.simulateActivity( day, 13, 0, 5, MEDIUM );
		    
		    state.simulateActivity( day, 13, 55, 5, QUIET );
		    state.simulateActivity( day, 14, 0, 5, RUSH_HOUR );
		    
		    state.simulateActivity( day, 16, 55, 5, RUSH_HOUR );
		    state.simulateActivity( day, 17, 0, 5, QUIET );
		    state.simulateActivity( day, 17, 5, 5, MEDIUM );
		}
		return state;
	}
	
	public static ContactAreaState biHourlyPattern() {
		ContactAreaState state = new ContactAreaState( 10 );
		for ( int day = 0; day < 10; day++ ) {
			for ( int hour = 0; hour < 24; hour++ ) {
				state.simulateActivity( day, hour, 15, 5, RUSH_HOUR );
				state.simulateActivity( day, hour, 40, 5, RUSH_HOUR );
			}
		}
		return state;
	}
	
	// COMPLEX PATTERNS
	
	public static ContactAreaState unevenBiHourlyPattern() {
		ContactAreaState state = new ContactAreaState( 10 );
		for ( int day = 0; day < 10; day++ ) {
			for ( int hour = 0; hour < 24; hour++ ) {
				state.simulateActivity( day, hour, 10, 5, RUSH_HOUR );
				state.simulateActivity( day, hour, 20, 5, MEDIUM );
			}
		}
		return state;
	}
	
	// NOISY PATTERNS
	
	public static ContactAreaState noisyHourlyPattern() {
		ContactAreaState state = new ContactAreaState( 10 );
		for ( int day = 0; day < 10; day++ ) {
			for ( int hour = 0; hour < 24; hour++ ) {
				double busyLevel = Math.random() <= 0.5 ?  MEDIUM : RUSH_HOUR;
				state.simulateActivity( day, hour, 30, 5, busyLevel );
			}
		}
		return state;
	}
	
	public static ContactAreaState noisyUnevenBiHourlyPattern() {
		ContactAreaState state = new ContactAreaState( 10 );
		for ( int day = 0; day < 10; day++ ) {
			for ( int hour = 0; hour < 24; hour++ ) {
				double rushLevel = Math.random() > 0.5 ? RUSH_HOUR : MEDIUM;
				double quietLevel = Math.random() > 0.5 ? MEDIUM : QUIET;
				state.simulateActivity( day, hour, 10, 5, rushLevel );
				state.simulateActivity( day, hour, 50, 5, quietLevel );
			}
		}
		return state;
	}
	
	public static ContactAreaState createHourlyPattern( String[] times, String[] levels ) {
		ContactAreaState state = new ContactAreaState( 10 );
		for ( int day = 0; day < 10; day++ ) {
			for ( int hour = 0; hour < 24; hour++ ) {
				for ( int startMin = 0; startMin < times.length; startMin++ ) {
					int min = Integer.parseInt( times[startMin] );
					double level = Double.parseDouble( levels[startMin] );
					state.simulateActivity( day, hour, min, 5, level );
				}
			}
		}
		return state;
	}
}
