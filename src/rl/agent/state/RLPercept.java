/**
 * 
 */
package rl.agent.state;

import aima.core.agent.Percept;


/**
 * The state of a single reinforcement learning agent.
 * <p>These states can be aggregated to reduce the size of the
 *    state space.</p>
 * 
 * @author James Giller 109466711
 */
public interface RLPercept extends Percept {

	/**
	 * Calculate the distance between this state and another.
	 * <p>States that are less than a threshold distance from each
	 * other can be aggregated.</p>
	 * 
	 * @param sPrime A state to measure distance from.
	 * @return A measure of the distance between this and sPrime.
	 */
	abstract double distance(RLPercept sPrime);

}