/**
 * 
 */
package misc;

import dirl.action.DIRLAction;
import dirl.action.RewardFunction;
import dirl.agent.state.DIRLPercept;
import discovery.action.DiscoveryDutyCycle;

/**
 * 
 * 
 * @author James Giller 109466711
 */
public class RADADiscoveryAction implements DIRLAction {
	
	private int priority;
	// True if this action is a "no operation" that does nothing.
	private boolean isNoOp;
	/**
	 * The duty cycle determines the frequency of broadcasting discovery beacons.
	 */
	private DiscoveryDutyCycle dutyCycle;
    
    /**
     * 
     * @param timeDomain The duration of a single execution of this task.
     * @param dutyCycle The duty cycle to operate at when broadcasting beacons.
	 * @param priority The priority of this action.
	 * @param isNoOp Whether or not this action should do nothing.
     */
	public RADADiscoveryAction( int timeDomain, DiscoveryDutyCycle dutyCycle, int priority, boolean isNoOp ) {
		//this.timeDomain = timeDomain;
		this.dutyCycle = dutyCycle;
		this.priority = priority;
		this.isNoOp = isNoOp;
		//rf = new RADARewardFunction();
	}
	
	/**
	 * Set the context in which this action is executed.
	 * @param context Discovery tasks are executed in pedestrian environments.
	 */
	public void setContext( PedEnvState context ) {
		//this.context = context;
	}
	
	/**
	 * @see aima.core.agent.Action#isNoOp()
	 */
	@Override
	public boolean isNoOp() {
		return isNoOp;
	}

    /**
     * Discovery Actions are always applicable.
     * @param s The state of the agent executing this action.
     * @return true
     */
	@Override
	public boolean isApplicable(DIRLPercept s) {
		// always applicable.
		return true;
	}


	public int getPriority() {
		return this.priority;
	}

	/*@Override
	public RewardFunction getRewardFunction() {
		// TODO Auto-generated method stub
		return null;
	}*/

	/* (non-Javadoc)
	 * @see dirl.action.DIRLAction#execute()
	 *
	@Override
	public void execute() {
		return;
	}*/

	/*@Override
	double getReward() {
		// TODO Auto-generated method stub
		return 0;
	}*/

}
