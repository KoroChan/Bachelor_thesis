/**
 * 
 */
package discovery.agent.state;

import rl.agent.state.RLPercept;
import util.WeightedVariable;
import discovery.environment.TimeOfDay;

/**
 * The perceived state of a single wireless sensor node discovering mobile devices.
 * @author James Giller 109466711
 */
public class DiscoveryPercept implements RLPercept {
    /**
     * The minute of the hour.
     */
	private WeightedVariable<Integer, Double> minute = new WeightedVariable<>( null, 1.0 );
	/**
	 * The hour of the day.
	 */
	private WeightedVariable<Integer, Double> hour = new WeightedVariable<>( null, 0.0 );
	/**
	 * The percentage of battery remaining at the node.
	 */
	private double battery;
	/**
	 * The number of contacts made by the node at the time observed.
	 */
	private int numContactsMade;
	
	/**
	 * @param todWeight The importance of time of day.
	 * @param tod The time of day.
	 * @param batteryWeight The importance of the remaining battery.
	 * @param battery The percentage of battery remaining.
	 * @param numContactsMade The total number of contacts made by the node.
	 */
	DiscoveryPercept( TimeOfDay tod,
			          double battery, int numContactsMade ) {
        this.battery = battery;
        this.numContactsMade = numContactsMade;
        this.minute.setValue( tod.getMinute() );
        this.hour.setValue( tod.getHour() );
	}
	
	/**
	 * @return The battery percentage.
	 */
	public double getBattery() {
		return battery;
	}
	
	/**
	 * @return The number of contacts made by the node.
	 */
	public int getNumContacts() {
		return this.numContactsMade;
	}

	/** 
	 * The weights associated with this state's variables are used to calculate hamming distance.
	 * @see rl.agent.state.RLPercept#distance(rl.agent.state.RLPercept)
	 */
	public double hammingDistance(DiscoveryPercept sPrime) {
		double whd;
		DiscoveryPercept rsPrime = (DiscoveryPercept) sPrime;
		whd = 0;
		whd += minute.getWeight() * Math.abs( minute.getValue() - rsPrime.minute.getValue() );
		whd += hour.getWeight() * Math.abs( hour.getValue() - rsPrime.hour.getValue() );
		return whd;
	}
    
	/**
	 * @throws IllegalArgumentException if {@code sPrime} is not a {@link DiscoveryPercept}.
	 */
	@Override
	public double distance(RLPercept sPrime) throws IllegalArgumentException {
		if ( ! ( sPrime instanceof DiscoveryPercept ) ) {
			String sPrimeClass = sPrime.getClass().getName();
			String thisClass = this.getClass().getName();
			throw new IllegalArgumentException( "Cannot compare " + sPrimeClass + " with " + thisClass );
		}
		return hammingDistance( (DiscoveryPercept)sPrime );
	}
	
	@Override
	public String toString() {
		return String.format( "%02d:%02d", hour.getValue(), minute.getValue() );
	}
	
	public static void main( String args[] ) {
	    TimeOfDay a = new TimeOfDay( 1, 9, 0 );
	    TimeOfDay b = new TimeOfDay( 1, 10, 4 );
	    DiscoveryPercept ap = new DiscoveryPercept( a, 100, 0 );
	    DiscoveryPercept bp = new DiscoveryPercept( b, 100, 0 );
	    System.out.println( ap.hammingDistance( bp ) );
	}
}
