/**
 * 
 */
package discovery.action;

import rl.action.RewardFunction;
import discovery.agent.state.DiscoveryPercept;

/**
 * @author James Giller 109466711
 */
public class DiscoveryRewardFunction implements RewardFunction<DiscoveryPercept> {
	
	/**
	 * The reward is calculated as:
	 * <p>( ( &#945 * min( &#949, nc ) ) - ( &#946 * es )</p>
	 * <p>Where <em>nc</em> is the number of contacts made since
	 *    {@code s}, and <em>es</em> is the energy spent.</p>
	 */
	@Override
	public double rewardTransition(DiscoveryPercept s, DiscoveryPercept sPrime) {
		double energyDifference = s.getBattery() - sPrime.getBattery();
		int numContactsMade = sPrime.getNumContacts() - s.getNumContacts();
		return ( 10 * Math.min( 5, numContactsMade ) ) - ( 1000 * energyDifference );
		//return ( numContactsMade * 10 - 1 ) * energyDifference;
	}
}
