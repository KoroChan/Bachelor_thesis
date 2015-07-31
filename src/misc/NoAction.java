/**
 * 
 */
package misc;

import dirl.action.DIRLAction;
import dirl.action.RewardFunction;
import dirl.agent.state.DIRLPercept;

/**
 * An action that does nothing.
 * @author James Giller 109466711
 */
public class NoAction implements DIRLAction {
    
	/**
	 * 
	 */
	public static final NoAction NO_ACTION = new NoAction();
	
	protected NoAction() {
		
	}
	
	/**
	 * @return {@code true}
	 * @see aima.core.agent.Action#isNoOp()
	 */
	@Override
	public boolean isNoOp() {
		return true;
	}

	/**
	 * @return {@code true}
	 * @see dirl.action.DIRLAction#isApplicable(dirl.agent.state.DIRLPercept)
	 */
	@Override
	public boolean isApplicable(DIRLPercept s) {
		return true;
	}

	/**
	 *  @return {@code null}
	 * @see dirl.action.DIRLAction#getRewardFunction()
	 */
	/*@Override
	public RewardFunction getRewardFunction() {
		// TODO Auto-generated method stub
		return null;
	}*/
}
