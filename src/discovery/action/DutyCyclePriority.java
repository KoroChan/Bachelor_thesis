/**
 * 
 */
package discovery.action;

import rl.action.PriorityOrder;

/**
 * @author James Giller 109466711
 */
public class DutyCyclePriority implements PriorityOrder<DiscoveryDutyCycle> {

	@Override
	public int compare(DiscoveryDutyCycle a1, DiscoveryDutyCycle a2) {
		return a2.priority() - a1.priority();
	}

}
