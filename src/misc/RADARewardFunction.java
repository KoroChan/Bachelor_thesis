/**
 * 
 */
package misc;

import dirl.action.RewardFunction;


/**
 * The reward function defined for RADA discovery actions.
 * A framework for Resource-Aware Data Accumulation in sparse wireless sensor networks.
 * <pre>For each task, the reward is positive if 
 * at least one MDC is successfully detected. If no MDC is detected,
 * the reward is negative (equal to minus [energy spent].</pre>
 * 
 * @author James Giller 109466711
 */
public class RADARewardFunction implements RewardFunction {
	
	/**
	 * Calculate the reward for executing a RADA discovery action.
	 * 
	 * @param nc The number of contacts detected while executing that task.
	 * @param mp A price multiplier.
	 * @param es The energy spent.
	 * @return The reward.
	 */
	public double getReward( int nc, int mp, int es ) {
		return (nc * mp - 1) * es;
	}
    
}
