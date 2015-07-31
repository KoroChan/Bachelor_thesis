/**
 * 
 */
package rl.action;

import java.util.Comparator;

/**
 * Defines a priority ordering over actions.
 * 
 * <p>Priority can be used as a tie-breaker in cases where
 * <ul>
 *    <li>No utilities have yet been associated with a set of actions.</li>
 *    <li>Multiple actions have equal utility values.</li>
 * </ul></p>
 * @author James Giller 109466711
 */
public interface PriorityOrder<A extends RLAction> extends Comparator<A> {

	/**
	 * Determine priority of a1 relative to a2.
	 * @return A negative integer, zero, or a positive integer as {@code a1} is
	 *         of lesser, equal, or greater priority than {@code a2}. 
	 */
	@Override
	int compare(A a1, A a2);
    
}
