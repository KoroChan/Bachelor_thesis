/**
 * 
 */
package discovery.environment;

import discovery.agent.NodeAgent;
import discovery.action.DiscoveryDutyCycle;
import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractEnvironment;

/**
 * @author James Giller 109466711
 */
public class PedestrianEnvironment extends AbstractEnvironment {
	
	private NodeAgent agent;
	private ContactAreaState currState;
	
	/**
	 * @param agent A wireless sensor node.
	 * @param contactArea The state of the contact area for {@code agent}.
	 */
	public PedestrianEnvironment( NodeAgent agent, ContactAreaState contactArea ) {
		this.agent = agent;
		addAgent( agent );
		this.currState = contactArea;
		this.agent.setContactArea( currState );
	}
	
	/**
	 * Execute N trials.
	 * 
	 * @param n
	 *            the number of trials to execute.
	 */
	public void executeTrials(int n) {
		for (int i = 0; i < n; i++) {
			executeTrial();
			currState.reset();
			agent.reset();
		}
	}

	/**
	 * Execute a single trial.
	 */
	public void executeTrial() {
		for (Agent a : agents) {
			a.setAlive(true);
		}
		stepUntilDone();
	}

	@Override
	public void step() {
		if (agent.isAlive()) {
			Action anAction = agent.execute( getPerceptSeenBy(agent) );
			EnvironmentState es = executeAction( agent, anAction );
			updateEnvironmentViewsAgentActed( agent, anAction, es );
		}
	}
	
	@Override
	public EnvironmentState executeAction(Agent anAgent, Action action) {
		if ( ! action.isNoOp() ) {
		    if ( anAgent == agent && action instanceof DiscoveryDutyCycle ) {
			    agent.setDutyCycle( (DiscoveryDutyCycle) action );
			    agent.discover();
		    }
		}
		return currState;
	}

	@Override
	public EnvironmentState getCurrentState() {
		return currState;
	}

	@Override
	public Percept getPerceptSeenBy(Agent anAgent) {
		if ( anAgent == agent ) {
			return agent.getCurrentPercept();
		} else {
			return null;
		}
	}
	
}
