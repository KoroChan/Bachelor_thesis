/**
 * 
 */
package discovery.agent;

import rl.agent.ExplorationProbabilityFunction;
import discovery.agent.state.DiscoveryPercept;

/**
 * @author James Giller 109466711
 */
public class DiscoveryEPF implements ExplorationProbabilityFunction<DiscoveryPercept> {
	
	private int maxNumContacts;
	
	public DiscoveryEPF( int maxNumContacts ) {
		this.maxNumContacts = maxNumContacts;
	}
    
	/**
	 * The probability to explore is calculated as:
	 * <pre>minProb + max( 0, ( maxProb - minProb ) * ( c<sub>max</sub> - c ) / c<sub>max</sub> )</pre>
	 * <p>Where <em>c<sub>max</sub></em> represents the maximum number of contacts for the learning policy, and
	 * <em>c</em> the number of contacts detected by the agent at the time of evaluation
	 */
	@Override
	public double getProbExplore( DiscoveryPercept s, double minProb, double maxProb) {
		int numContactsMade = ((DiscoveryPercept) s).getNumContacts();
		return minProb + Math.max( 0, ( maxProb - minProb ) * ( maxNumContacts - numContactsMade) / maxNumContacts );
	}

}
