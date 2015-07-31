/**
 * 
 */
package rl.action;

/**
 * Calculates the reward for executing an action.
 * @param S Rewards are calculated objectively from changes in state.
 * @author James Giller 109466711
 */
public interface RewardFunction<S> {
    
	/**
	 * Calculate the reward for making transition from state {@code s} to {@code sPrime}.
	 * @param s A previous state.
	 * @param sPrime A new state arrived at from {@code s}.
	 * @return The reward for going from {@code s} to {@code sPrime}.
	 */
	double rewardTransition( S s, S sPrime );
}
