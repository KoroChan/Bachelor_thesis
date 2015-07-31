/**
 * 
 */
package rl.action;

import java.util.LinkedHashSet;
import java.util.Set;

import rl.agent.state.RLPercept;

import aima.core.probability.mdp.ActionsFunction;

/**
 * Returns the actions applicable in any state of an agent, and their associated reward functions.
 * @param S The state type.
 * @param A The action type.
 * @author James Giller 109466711
 */
public abstract class RLActionsFunction<S extends RLPercept, A extends RLAction> implements ActionsFunction<S, A> {
	
	// use a LinkedHashSet to preserve insertion order of actions.
	protected Set<A> allActions = new LinkedHashSet<A>();
	
	/**
	 * @param s A state.
	 * @return A set of actions applicable in {@code state}.
	 */
	@Override
	public Set<A> actions( S s ) {
		LinkedHashSet<A> applicableActions = new LinkedHashSet<>();
	    for ( A action : allActions ) {
	    	if ( action.isApplicable( s ) ) {
	    		applicableActions.add( action );
	    	}
	    }
	    return applicableActions;
	}
	
	/**
	 * @param action An action.
	 * @return The reward function associated with {@code action}.
	 */
	public abstract RewardFunction<S> getRewardFunctionfor( A action );

}
