/**
 * 
 */
package misc;

import dirl.agent.DIRLProgram;
import discovery.agent.EnergyCostModel;
import discovery.agent.ModeOfOperation;
import discovery.agent.state.DiscoveryPercept;
import aima.core.agent.impl.AbstractAgent;

/**
 * A wireless sensor node agent that attempts to discover mobile devices.
 * @author James Giller 109466711
 */
public class DiscoveryAgent extends AbstractAgent {
	
	/*
	 * Default weight associated with inter-contact time.
	 */
	private static final double ICT_WEIGHT = 0.005;
	/*
	 * Default weight associated with the in-range flag.
	 */
	private static final double IR_WEIGHT = 1.0;
	/*
	 * Default weight associated with the time of day.
	 */
	private static final double TOD_WEIGHT = 0.0;
	
	/**
	 * The internal state of the agent.
	 */
	private DiscoveryPercept state;
	/**
	 * The duration of actions scheduled by the agent program.
	 */
	private int timeDomain;
	/**
	 * To calculate the energy cost of actions.
	 */
	private EnergyCostModel costModel;
	
	/**
	 * Initialise a new rational agent.
	 * @param program A reinforcement learning program for the agent to run.
	 * @param initialTimeDomain An initial measure of time over which to
	 *        execute actions scheduled by the {@code program}.
	 */
	public DiscoveryAgent( DIRLProgram<?, ?> program, int initialTimeDomain ) {
		super( program );
		state = new DiscoveryPercept( ICT_WEIGHT, IR_WEIGHT, TOD_WEIGHT );
		this.timeDomain = initialTimeDomain;
	}
	
	/**
	 * @return The current time domain duration.
	 */
	public int getTimeDomain() {
		return timeDomain;
	}

	/**
	 * Contact probing consists of broadcasting a special BEACON message,
	 * followed by listening for responses over some time.
	 * @param timeToListen The length of time to listen for responses to a BEACON.
	 */
	public void contactProbe( int timeToListen ) {
		drainBattery( ModeOfOperation.TRANSMIT, 1 );
		drainBattery( ModeOfOperation.RECEIVE, timeToListen );
	}
	
	/**
	 * Whether the agent is considered to be in contact with a mobile device and
	 * can begin data transmission.
	 */
	public void setInContact() {
		state.addContact();
	}
	
	/**
	 * The agent can take advantage of its environment by conserving energy when
	 * no contacts can/need to be made.
	 * @param duration The length of time to sleep.
	 */
	 public void sleep( int duration ) {
		 drainBattery( ModeOfOperation.SLEEP, duration );
	 }
	 
	 /**
	  * @return The state of the agent.
	  */
	 public DiscoveryPercept getState() {
		 return this.state;
	 }
	 
	// PRIVATE METHODS //
		
	private void drainBattery( ModeOfOperation mode, int duration ) {
		if ( isAlive() ) {
			// calculate energy cost.
			double energyCost = costModel.getCost( mode, duration );
			// subtract energy.
			state.setBattery( state.getBattery() - energyCost );
			if ( state.getBattery() <= 0 ) {
				state.setBattery( 0 );
				setAlive( false );
				return;
			}
		}
	}
}
