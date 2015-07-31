/**
 * 
 */
package discovery.agent.state;

import discovery.action.DiscoveryDutyCycle;
import discovery.agent.ModeOfOperation;
import discovery.environment.TimeOfDay;

/**
 * An instance in the history of a node discovering mobile devices.
 * @author James Giller 109466711
 */
public class HistoryInstance {
    /**
     * The node's mode of operation.
     */
	private final ModeOfOperation mode;
	/**
	 * An action, that was chosen to perform.
	 */
	private final DiscoveryDutyCycle actionChosen;
    /**
     * Did the node make a contact?
     */
    private final boolean madeContact;
    /**
     * The battery in the node at this time.
     */
    private final double batteryRemaining;
    /**
     * The time of day.
     */
    private final TimeOfDay timeOfDay;
    
    

	/**
	 * @param nodeIsAsleep The node's mode of operation.
	 * @param actionChosen The action executed by the node.
	 * @param timeDomain The time domain duration.
	 * @param madeContact Did the node make contact?
	 * @param isNewContact Was any contact made new or a re-discovery?
	 * @param batteryRemaining The remaining battery percentage.
	 * @param timeOfDay The time of day in 5-minute intervals.
	 */
	public HistoryInstance( ModeOfOperation mode,
			DiscoveryDutyCycle actionChosen,boolean madeContact,
			double batteryRemaining, TimeOfDay timeOfDay) {
		this.mode = mode;
		this.actionChosen = actionChosen;
		this.madeContact = madeContact;
		this.batteryRemaining = batteryRemaining;
		this.timeOfDay = timeOfDay;
	}

	/**
	 * @return The node's mode of operation.
	 */
	public ModeOfOperation getMode() {
		return mode;
	}
	
	/**
	 * @return The action executed by the node.
	 */
	public DiscoveryDutyCycle getActionChosen() {
		return actionChosen;
	}

	/**
	 * @return Did the node make contact?
	 */
	public boolean isMadeContact() {
		return madeContact;
	}

	/**
	 * @return The remaining battery percentage.
	 */
	public double getBatteryRemaining() {
		return batteryRemaining;
	}

	/**
	 * @return The time of day as the <em>n<sup>th</sup></em> five minute interval.
	 */
	public TimeOfDay getTimeOfDay() {
		return timeOfDay;
	}
    
	@Override
	public String toString() {
		String result = "";
		result += timeOfDay + ", ";
		result += mode + ", ";
		result += madeContact + ", ";
		result += batteryRemaining;
		return result;
	}
}
