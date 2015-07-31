/**
 * 
 */
package discovery.environment;

import java.util.BitSet;

import aima.core.agent.EnvironmentState;

/**
 * The state of the contact area around a wireless sensor node.
 * <p>Time is divided into episodes in which a node can attempt 
 * associating with a pedestrian's mobile device or sleep. When associating,
 * the node broadcasts a beacon and waits for a response.
 * In any episode, a contact may or may not be available.</p>
 * <p>The very first episode is considered to be midnight 00:00 on a Monday.</p>
 * @author James Giller 109466711
 */
public class ContactAreaState implements EnvironmentState {
	
	/*
	 * A day's worth of episodes.
	 */
	private static final int A_DAY = 86400;
	/*
	 * An hour's worth of episodes.
	 */
	private static final int AN_HOUR = 3600;
	/*
	 * A minute's worth of episodes.
	 */
	private static final int A_MINUTE = 60;
	
	/**
	 * A time-line of arrivals of pedestrians to the contact area of a node.
	 */
    private BitSet episodes;
    /**
     * The current episode.
     */
    private int currEpisode = 0;
    /*
     * Terminate simulation at the right time.
     */
    private final int numEpisodes;
    
    /**
     * Create a model of the environment over a certain number of days.
     * <p>Initially, there are no contacts are available at any time.</p>
     * @param numDays
     */
    public ContactAreaState( int numDays ) {
    	this.episodes = new BitSet( numDays * A_DAY );
    	currEpisode = 0;
    	numEpisodes = numDays * A_DAY;
    }
    
    /**
     * Sleep for {@code n} episodes and ignore any possible contacts.
     * @param n
     */
    public void skip( int n ) {
    	currEpisode += n;
    }
    
    /**
     * Probe for a pedestrian in the contact area.
     * @return True if there was at least a single pedestrian in the contact area, 
     * otherwise false.
     */
    public boolean probe() {
    	return ( episodes.get( currEpisode++ ) );
    }
    
    public TimeOfDay timeOfDay() {
    	int minute = currEpisode / 60;
    	int minuteOfHour = minute % 60;
    	int hourOfDay = ( minute % 1440 ) / 60;
    	int dayOfWeek = ( currEpisode / A_DAY ) % 7;
    	TimeOfDay tod = new TimeOfDay( dayOfWeek, hourOfDay, minuteOfHour );
    	return tod;
    }
    
    /**
     * Simulate human activity in the contact area of a node over a certain time interval.
     * @param day The day of activity.
     * @param hour The hour of activity.
     * @param minute The minute of activity.
     * @param numMinutes The number of minutes of activity to simulate.
     * @param p The percentage of bits that will be set to 1. (The level of activity)
     */
    void simulateActivity( int day, int hour, int minute, int numMinutes, double p ) {
		int start = ( day * A_DAY ) + ( hour * AN_HOUR ) + ( minute * A_MINUTE );
		int finish = start + ( numMinutes * A_MINUTE );
		for ( int i = start; i < finish; i++ ) {
			if ( Math.random() < p ) {
				episodes.set( i );
			}
		}
	}
    
    public boolean finished() {
    	return currEpisode >= (numEpisodes - 1 );
    }
    
    void reset() {
    	currEpisode = 0;
    }
}
