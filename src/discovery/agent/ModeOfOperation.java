/**
 * 
 */
package discovery.agent;

/**
 * The modes of operation that a Wireless Sensor Node can enter.
 * @author James Giller 109466711
 */
public enum ModeOfOperation {
	/**
	 * The node is in a high power state, contact probing.
	 */
    PROBE( 3.391049e-4 ),
	/**
	 * The node is in a low power state.
	 */
	SLEEP( 3.8421757e-6 ),
    /**
     * The node is turned off completely.
     */
	POWER_OFF( 0 );

	
	private final double costPerMs;
	
	private ModeOfOperation( double costPerMs ) {
		this.costPerMs = costPerMs;
	}
	
	/**
	 * @return The energy cost per millisecond in this state.
	 */
	public double cost() {
		return this.costPerMs;
	}
}
