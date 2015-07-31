/**
 * 
 */
package rl.action;

import rl.agent.state.RLPercept;
import aima.core.agent.Action;

/**
 * An action that can be performed by a reinforcement learning agent.
 * <p>An applicability predicate determines the states this action can be
 *    performed in. A priority ordering can be defined over actions. A
 *    reward function can be associated with actions.</p>
 * @author James Giller 109466711
 * @see rl.action.PriorityOrder
 * @see rl.action.RLActionsFunction
 */
public interface RLAction extends Action {
	/**
	 * @return Can this action be executed by an agent in state s?
	 */
	boolean isApplicable( RLPercept s );
	
}
