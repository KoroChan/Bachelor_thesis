/**
 * 
 */
package rl.agent;

import rl.agent.state.RLPercept;

/**
 * Calculates the probability for a reinforcement learning program to explore new actions.
 * @author James Giller 109466711
 */
public interface ExplorationProbabilityFunction<S extends RLPercept> {
    /**
     * Calculate the probability of exploring a new action while in a given state.
     * @param s The perceived state of an agent.
     * @param minProb The minimum probability to perform exploration.
     * @param maxProb The maximum probability to perform exploration.
     * @return A probability to perform exploration between minProb & maxProb.
     */
	double getProbExplore( S s, double minProb, double maxProb );
}
