/**
 * 
 */
package misc;

import aima.core.agent.EnvironmentState;

/**
 * The state of the contact area for a single sensor node in a
 * pedestrian environment at any time.
 * 
 * @author James Giller 109466711
 */
public class PedEnvState implements EnvironmentState {
	
	/**
	 * A time-line, each cell represents a time unit and 
	 * contains the number of pedestrians present at that time.
	 */
    private int[] arrivals;
    /**
     * The current time in this pedestrian environment.
     */
    private int t;
    
    /**
     * Create a new state of the contact area.
     * @param arrivals The pattern of pedestrian movement through the area.
     */
    public PedEnvState( int[] arrivals ) {
    	this.arrivals = arrivals;
    	this.t = 0;
    }

	/**
	 * Get the current time in this environment.
	 * @return t: the number of time units passed since this state was created.
	 */
	public int getCurrTime() {
		return t;
	}
    
    
}
