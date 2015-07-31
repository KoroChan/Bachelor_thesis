/**
 * 
 */
package discovery.action;

import rl.action.RLActionsFunction;
import rl.action.RewardFunction;
import discovery.agent.state.DiscoveryPercept;

/**
 * <p>All actions have the same 
 * {@link discovery.action.DiscoveryRewardFunction RewardFunction}.</p>
 * @author James Giller 109466711
 * @see discovery.action.DiscoveryDutyCycle
 */
public class DiscoveryActionsFunction extends RLActionsFunction<DiscoveryPercept, DiscoveryDutyCycle> {
	private DiscoveryRewardFunction rf;
	
	/**
	 * Available actions are
	 * <ul>
	 *    <li>High Duty Cycle: every 6s</li>
	 *    <li>Low Duty Cycle: every 12s</li>
	 *    <li>Sleep: No probes</li>
	 * </ul> 
	 */
	public DiscoveryActionsFunction() {
		rf = new DiscoveryRewardFunction();
		for ( DiscoveryDutyCycle dc : DiscoveryDutyCycle.values() ) {
			allActions.add( dc );
		}
	}

	@Override
	public RewardFunction<DiscoveryPercept> getRewardFunctionfor(
			DiscoveryDutyCycle action) {
		return rf;
	}

}
