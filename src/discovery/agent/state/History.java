/**
 * 
 */
package discovery.agent.state;

import discovery.HistoryFullException;

/**
 * An internal model of application and system variables maintained by a node.
 * @author James Giller 109466711
 */
public class History {
	/**
	 * The historical state and behaviour of the node.
	 */
	private HistoryInstance[] history;
	/**
	 * Where to record the latest history.
	 */
	private int nextInstance = 0;
	/**
	 * The total number of contacts made in history.
	 */
	private int totalNumContacts = 0;
	
	/**
	 * Create a new model that can track history to given size.
	 * @param historySize
	 */
	public History( int historySize ) {
		history = new HistoryInstance[historySize];
	}
	
	/**
	 * Update the model with new history.
	 * @param instance
	 * @throws HistoryFullException if the new instance of history cannot be
	 *         stored.
	 */
	public void updateModel( HistoryInstance instance ) {
		if ( ! historyFull() ) {
			if ( instance.isMadeContact() ) {
				totalNumContacts++;
			}
			history[nextInstance++] = instance;
		} else {
			throw new HistoryFullException();
		}
	    //System.out.println( instance );
	}
	
	/**
	 * @return True if no more history can be recorded.
	 */
	public boolean historyFull() {
		return nextInstance >= history.length;
	}
	
	/**
	 * @return True if there is no recorded history.
	 */
	public boolean historyEmpty() {
		return nextInstance == 0;
	}
	
	/**
	 * Generate a percept from the recorded history.
	 * @return
	 */
	public DiscoveryPercept generatePercept() {
		if ( ! historyEmpty() ) {
			DiscoveryPercept percept;
			HistoryInstance lastRecord = history[nextInstance - 1];
			percept = new DiscoveryPercept( lastRecord.getTimeOfDay(),
					                        lastRecord.getBatteryRemaining(),
					                        totalNumContacts );
			return percept;
		}
		return null;
	}
	
	public void reset() {
		int historySize = this.history.length;
		this.history = new HistoryInstance[historySize];
		this.nextInstance = 0;
		this.totalNumContacts = 0;
	}
}
