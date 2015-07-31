/**
 * 
 */
package discovery.agent;

import rl.agent.RLProgram;
import discovery.HistoryFullException;
import discovery.OutOfBatteryException;
import discovery.action.DiscoveryDutyCycle;
import discovery.agent.state.HistoryInstance;
import discovery.agent.state.DiscoveryPercept;
import discovery.agent.state.History;
import discovery.environment.ContactAreaState;
import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;

/**
 * A wireless sensor node that discovers mobile devices.
 * @author James Giller 109466711
 */
public class NodeAgent extends AbstractAgent {
	
	/**
	 * The length of time over which discovery actions are performed.
	 * <p>5 minutes of simulated time.</p>
	 */
	private static final int ACTION_DURATION = 300;
	
	/**
	 * The internal history of the node's actions.
	 */
	private History history;
    /**
     * The contact area at the node.
     */
	private ContactAreaState contactArea;
	/**
	 * The mode of operation the node is in.
	 */
	private ModeOfOperation mode = ModeOfOperation.POWER_OFF;
	/**
	 * The duty cycle the node is operating.
	 */
	private DiscoveryDutyCycle dutyCycle;
	/**
	 * Whether the node is in contact with a mobile device.
	 */
	private boolean isInContact = false;
	/**
	 * The percentage of battery remaining.
	 */
	private double battery = 100;
	/**
	 * The length of time to spend learning, after which then node will turn off.
	 * One week.
	 */
	private int learningTime = 2880;
	
	/**
	 * @param program A reinforcement learning program for the node to run.
	 * @param historySize The capacity of the node's internal history history.
	 */
	public NodeAgent( RLProgram<DiscoveryPercept, DiscoveryDutyCycle> program,
			               int historySize ) {
		super( program );
		history = new History( historySize );
	}
    
	/**
	 * Set the contact area for this node.
	 * @param contactArea Represents the contacts that pass through the contact
	 *        area of this node.
	 */
	public void setContactArea( ContactAreaState contactArea ) {
		this.contactArea = contactArea;
	}
	
	/**
	 * Set the duty cycle this node will operate under when performing discovery
	 * actions.
	 * @param dutyCycle
	 */
	public void setDutyCycle( DiscoveryDutyCycle dutyCycle ) {
		this.dutyCycle = dutyCycle;
	}
	
	@Override
	public Action execute( Percept p) {
		if ( isAlive() && program != null ) {
			try {
				Action a = program.execute(p);
				return a;
			} catch ( RuntimeException ex ) {
				throw ex;
			}
		}
		return null;
	}
	
	/**
	 * Perform the currently chosen discovery action (duty cycle) for a single
	 * time domain.
	 */
	public void discover() {
		if ( isAlive() ) {
			//System.out.println( dutyCycle );
			if ( contactArea.finished () ) {
				setAlive( false );
			}
			try {
				int i = ACTION_DURATION;
				while ( i > 0 ) {
					// Cut the action short if it will exceed ACTION_DURATION.
			    	int probesToSkip = ( i < dutyCycle.getToff() ) ? i : dutyCycle.getToff();
			    	enterMode( ModeOfOperation.SLEEP, probesToSkip );
			    	contactArea.skip( probesToSkip );
			    	recordHistory();
			    	i -= probesToSkip;
					if ( i > 0 ) {
					    enterMode( ModeOfOperation.PROBE, 1 );
					    isInContact = contactArea.probe();
					    recordHistory();
					    isInContact = false;
					    i--;
				    }
				}
				learningTime--;
				if ( learningTime == 0 ) {
					setAlive( false );
				}
			} catch ( OutOfBatteryException e ) {
				throw e;
			} catch ( HistoryFullException hfe ) {
				throw hfe;
			}
			//System.out.println( "\n" );
		}
	}
	
	/**
	 * @return The current perceived state of this node.
	 */
	public DiscoveryPercept getCurrentPercept() {
		return history.generatePercept();
	}
	
	@Override
	public void setAlive(boolean alive) {
		super.setAlive( alive );
		if ( ! alive ) {
			this.mode = ModeOfOperation.POWER_OFF;
			System.out.println( "\n" + ((RLProgram)this.program).utilityTable() );
			System.out.println( "\n" + battery );
		} else {
			enterMode( ModeOfOperation.SLEEP, 0 );
		}
		recordHistory();
	}
	
	
	public void reset() {
		this.battery = 100;
		this.dutyCycle = null;
		this.learningTime = 2880;
		this.isInContact = false;
		this.mode = ModeOfOperation.POWER_OFF;
		((RLProgram)this.program).reset();
		this.history.reset();
	}
	
	// PRIVATE METHODS
	
	/*
	 * @throws OutOfBatteryException if battery would deplete before the total
	 *         duration.
	 */
	private void enterMode( ModeOfOperation mode, int duration ) {
		if ( isAlive() ) {
			boolean cantComplete = false;
			this.mode = mode;
			battery -= this.mode.cost() * duration;
			if ( battery < 0 ) cantComplete = true;
			if ( battery <= 0 ) {
				battery = 0;
				setAlive( false );
			}
			if ( cantComplete ) {
				throw new OutOfBatteryException();
			}
		}
	}
	
	/*
	 * @throws HistoryFullException if the new instance of history cannot be
	 *         stored.
	 */
	private void recordHistory() {
		HistoryInstance now;
		now = new HistoryInstance( mode, dutyCycle,
				                    isInContact, battery,
				                    contactArea.timeOfDay() );
		try {
			history.updateModel( now );	
		} catch ( HistoryFullException ex ) {
		    throw ex;	
		}
	}
}
