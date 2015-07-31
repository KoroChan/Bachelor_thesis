/**
 * 
 */
package discovery.action;

import rl.action.RLAction;
import rl.agent.state.RLPercept;
import discovery.agent.state.DiscoveryPercept;

/**
 * A duty cycle that determines the frequency of contact probing.
 * <p>Contact probing includes broadcasting a beacon and waiting for a response.
 *    These two actions always occur together, so are grouped into a single
 *    unit of work. The node will sleep for a multiple of units of time it takes
 *    to perform contact probing.</p>
 * @author James Giller 109466711
 */
public enum DiscoveryDutyCycle implements RLAction {
	HDC( 5, 4 ),
	LDC( 11, 3 ),
	ZERO( 300, 1 );
	
	/**
	 * The node will sleep for {@code toff} units of time, where
	 * each unit is the time taken to perform contact probing.
	 */
	private final int toff;
	
	private final int priority;
	
	DiscoveryDutyCycle( int toff, int priority ) {
		this.toff = toff;
		this.priority = priority;
	}

	/**
	 * @return How long the node should spend asleep.
	 */
	public int getToff() {
		return toff;
	}

	@Override
	public boolean isNoOp() {
		return false;
	}

	/**
	 * These actions are applicable as long as the node still has battery remaining.
	 */
	@Override
	public boolean isApplicable( RLPercept s ) {
		if ( s instanceof DiscoveryPercept ) {
			if ( ( (DiscoveryPercept)s ).getBattery() > 0 ) {
				return true;
			}
		}
		return false;
	}
	
	int priority() {
		return this.priority;
	}

}
