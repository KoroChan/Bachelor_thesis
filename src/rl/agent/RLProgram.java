/**
 * 
 */
package rl.agent;

import java.util.*;

import rl.StateSpaceFullException;
import rl.action.PriorityOrder;
import rl.action.RLAction;
import rl.action.RLActionsFunction;
import rl.action.RewardFunction;
import rl.agent.state.RLPercept;

import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;


/**
 * A program that executes a reinforcement learning algorithm
 * based on Distributed Independent Reinforcement Learning.
 * 
 * <p>A table maps states and actions performed in those states to 
 * real valued utility measures. The program will choose an action to
 * perform in any state by highest utility (exploit) or at random (explore).</p>
 * 
 * <p>States stored in the table are aggregations of many discrete states, which
 * reduces the size of the state space. Rewards are calculated from 
 * performing actions in discrete states and then accumulated over time in the table.
 * These are the utility measures.</p>
 * 
 * @author James Giller 109466711
 * @param S The perceived state of a reinforcement learning agent.
 * @param A The actions executed by a reinforcement learning agent.
 */
public class RLProgram<S extends RLPercept, A extends RLAction> 
                      implements AgentProgram {
	/*
	 * The table that maps (state, action) pairs to utility values.
	 */
	private Map<S, Map<A, Double>> utilityTable;
	/*
	 * The previous state.
	 */
	private S s = null;
	/*
	 * The previous aggregate state.
	 */
	private S aggregateS = null; 
	/*
	 * The previous action.
	 */
	private A a = null;
	/*
	 * Returns the applicable actions for any state.
	 */
	private RLActionsFunction<S, A> af;
	/*
	 * A priority ordering over actions.
	 */
	private PriorityOrder<A> ordering;
	/*
	 * The upper bound on distance between states for aggregation.
	 */
	private double threshold;
	/*
	 * The maximum size of the learning table.
	 */
	private final int maxNumStates;
	/*
	 * Calculates the probability to perform exploration.
	 */
	private final ExplorationProbabilityFunction<S> epf;
	/*
	 * The maximum probability to perform exploration.
	 */
	private final double maxProbExplore;
	/*
	 * The minimum probability to perform exploration.
	 */
	private final double minProbExplore;
	/*
	 * When incrementing utilities,
	 * controls the weight given to previously learned utilities.
	 */
	protected final double learningRate;
	/*
	 * When incrementing utilities,
	 * controls the weight given to expected future rewards.
	 */
	private final double discount;
	/*
	 * The minimum utility that can be attributed to an action.
	 */
	private final double minUtil = -17.916;
	
	/**
	 * @param af A function that maps states to sets of applicable actions.
	 * @param ordering A priority ordering over actions to be executed.
	 * @param maxNumStates The maximum number of states to consider for learning.
	 * @param threshold The upper bound on distance for aggregating states.
	 * @param maxProbExplore The maximum probability to perform exploration.
	 * @param minProbExplore The minimum probability to perform exploration.
	 * @param learningRate Controls the weight given to previously learned utilities.
	 * @param discount Controls the weight given to possible future rewards.
	 */
	public RLProgram( RLActionsFunction<S, A> af, PriorityOrder<A> ordering, 
			            int maxNumStates, double threshold,
			            ExplorationProbabilityFunction<S> epf,
			            double maxProbExplore, double minProbExplore,
			            double learningRate, double discount ) {
		this.af = af;
		this.ordering = ordering;
		this.maxNumStates = maxNumStates;
		this.threshold = threshold;
		this.epf = epf;
		this.maxProbExplore = maxProbExplore;
		this.minProbExplore = minProbExplore;
		this.learningRate = learningRate;
		this.discount = discount;
		initialiseUtilityTable();
	}
    
	/**
	 * Select an action to execute while in a given state.
	 * @param percept The perceived state of an agent.
	 * @throws NullPointerException if {@code percept} is null.
	 * @throws IllegalArgumentException if {@code percept} is 
	 * not of type {@link rl.agent.state.RLPercept RLPercept}.
	 * @throws StateSpaceFullException if the learning table is full.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Action execute(Percept percept) throws StateSpaceFullException {
		if ( percept == null ) {
			throw new NullPointerException( "Program was passed a null percept." );
		}
		if ( percept instanceof RLPercept ) {
			Action action;
			try {
				action = learning((S) percept);
			} catch ( StateSpaceFullException ex ) {
				throw ex;
			}
			if ( action == null ) {
				// Could not determine an action to perform.
				return new Action() {
					@Override
					public boolean isNoOp() {
						return true;
					}
				};
			} else {
				return action;
			}
		}
		throw new IllegalArgumentException(
			"Program requires a " + 
		    s.getClass().getName() + " to execute." );
	}
	
	// PROTECTED METHODS
	
	/*
	 * Use the Q-Learning algorithm to select an action to execute.
	 * 
	 * @param sPrime The current state of an agent.
	 * @return An action to execute in the current state.
	 */
	protected A learning( S sPrime ) {
		
		if ( isTerminal( sPrime ) ) {
			s = null;
			a = null;
			return null;
		}
		
		// can sPrime be aggregated into the table?
		S aggregateSPrime = aggregateState( sPrime );
		if ( aggregateSPrime == null ) {
			// cannot be aggregated.
			if (! reachedMaxNumStates() ) {
				// cannot be aggregated, can add new state.
				addToUtilityTable( sPrime );
				aggregateSPrime = sPrime;
			} else {
				// cannot be aggregated, cannot add new state.
				// Can try accommodateState(sPrime)
				System.out.println( utilityTable() );
				throw new StateSpaceFullException();
			}
		}
		
		if ( s != null ) {
			// Compute the reward.
			RewardFunction<S> rewardFunction = af.getRewardFunctionfor( a );
			double r = rewardFunction.rewardTransition( s, sPrime );
			// update table(s, a)
			updateUtility( aggregateS, a, aggregateSPrime, r );
		}
	
		// choose an action to execute.
		s = sPrime;
		aggregateS = aggregateSPrime;
		a = randPolicy( aggregateSPrime );
		
		return a;
	} 
	
	/*
	 * @param sPrime A discrete state.
	 * @return A key into the learning table, the aggregation of
	 *         {@code sPrime} and other similar states.
	 * @version 1
	 */
	protected S aggregateState( S sPrime ) {
		Set<S> aggregateStates = utilityTable.keySet();
		/* Iterate all aggregate states,
		   find the first state within the threshold hamming distance of sPrime. */
		S aggregate = null;
		for ( S as : aggregateStates ) {
			//System.out.println( "Comparing " + as + " & " + sPrime );
			if ( as.distance(sPrime) < threshold ) {
				//System.out.println( "CLOSE" );
				aggregate = as;
				break;
			}
		}
		return aggregate;
	}
	
	/*
	 * Add a new state into the table.
	 * @param newState
	 */
	protected void addToUtilityTable( S newState ) {
		Set<A> actions = af.actions( newState );
		// actions might be null. What to do?
		Map<A, Double> actionUtilities = freshUtilitiesFor( actions );
		utilityTable.put( newState, actionUtilities );
	}
	
	/*
	 * @return The probability to perform exploration, at the time this method was called.
	 */
	protected double probExplore() {
		if ( epf != null ) {
			return epf.getProbExplore( s, minProbExplore, maxProbExplore );	
		} else {
			return minProbExplore;
		}
	}
    
	/*
	 * Choose a task uniformly at random that is applicable in {@code s}.
	 * @param s The state in which the action will be executed.
	 * @return An action to execute.
	 * @version 2
	 */
	protected A explore( S s ) {
		/* Generate a number uniformly at random to identify an action */
		A nextAction = null;
		Random rand = new Random();
		Set<A> availableActions = utilityTable.get( s ).keySet();
		int numActions = availableActions.size();
		int next = 0;
		int choice = rand.nextInt( numActions );
		for ( A action : availableActions ) {
			if ( next++ == choice ) {
				nextAction = action;
			}
			if ( nextAction != null ) break;
		}
		return nextAction;
	}
	
	/*
	 * Choose a task to execute based on learned utilities.
	 * <p>If actions are found with equal utilities, priority will be
	 * used to decide the action to execute.</p>
	 * @param s The state in which the task will be executed.
	 * @return An action to execute.
	 */
	protected A exploit( S s ) {
		A nextAction = null;
		Set<A> bestActions = argMaxUtilA( s );
		for ( A action : bestActions ) {
			if ( nextAction == null || ordering.compare( action, nextAction ) > 0 ) {
				nextAction = action;
			}
		}
		return nextAction;
	}
	
	/*
	 * Update the utility for executing {@code a} while in {@code s} arriving at {@code sPrime}.
	 * @param s State in which {@code a} was executed.
	 * @param a Action just executed.
	 * @param sPrime State arrived at after executing {@code a}.
	 * @param r The reward for executing {@code a} in {@code s}.
	 */
	protected void updateUtility( S s, A a, S sPrime, double r ) {
		Double currSUtil = utilityTable.get( s ).get( a );
		Double maxUtilSPrime = maxUtil( sPrime );
		Double newSUtil = (1-learningRate)*currSUtil + learningRate*((discount*maxUtilSPrime) + r);
		utilityTable.get( s ).put( a, newSUtil );
	}
	
	/*
	 * Test whether a state is terminal.
	 * <p>A state is terminal if it is not possible to transition to another state.</p>
	 * @param s A state to test.
	 * @return True if there are no actions applicable in state {@code s}.
	 */
	protected boolean isTerminal( S s ) {
		boolean terminal = false;
		if ( af.actions(s).size() == 0 ) {
			// No actions possible in state is considered terminal.
			terminal = true;
		}
		return terminal;
	}
	
	// PRIVATE METHODS
	
	private void initialiseUtilityTable() {
		this.utilityTable = new LinkedHashMap<S, Map<A, Double>>( maxNumStates );
	}
	
	/*
	 * @return True if the learning table has reached its maximum size.
	 */
	private boolean reachedMaxNumStates() {
		return utilityTable.size() >= maxNumStates;
	}
	
	/*
	 * @param s A state.
	 * @return The maximum expected utility of being in state {@code s}.
	 */
	private double maxUtil( S s ) {
		double max = Double.NEGATIVE_INFINITY;
		for ( Double util : utilityTable.get( s ).values() ) {
			if ( util > max ) max = util;
		}
		return max;
	}
	
	/*
	 * @param s A state.
	 * @return The set of actions with the highest utilities in state {@code s}.
	 */
	private Set<A> argMaxUtilA( S s ) {
		Set<A> bestActions = new HashSet<A>();
		double max = Double.NEGATIVE_INFINITY;
		for ( Map.Entry<A, Double> actionUtility : utilityTable.get( s ).entrySet() ) {
			Double candidateUtility = actionUtility.getValue();
			A candidate = actionUtility.getKey();
			if ( candidateUtility > max ) {
				bestActions.clear();
				max = candidateUtility;
				bestActions.add( candidate );
			} else if ( candidateUtility == max ) {
				bestActions.add( candidate );
			}
		}
		return bestActions;
	}
	
	/*
	 * @return A map of actions from the given set to initial utility values of 0.0
	 */
	private Map<A, Double> freshUtilitiesFor( Set<A> actions ) {
		int numActions = actions.size();
		HashMap<A, Double> utilities = new HashMap<A, Double>( numActions );
		for ( A action : actions ) {
			utilities.put( action, 0.0 );
		}
		return utilities;
	}
	
	/*
	 * @param probExplore
	 * @return {@code true} if exploration should be performed, otherwise {@code false}.
	 */
	private boolean shouldExplore( double probExplore ) {
		return Math.random() < probExplore;
	}
	
	/*
	 * Decide on an action to perform in state {@code s} using exploration or exploitation.
	 * @param s The current state of the agent.
	 * @return An action to perform in state {@code s}.
	 */
	private A policy( S s ) {
		boolean willExplore = shouldExplore( probExplore() );
		return ( willExplore ? explore( s ) : exploit( s ) );
	}
	
	private A randPolicy( S s ) {
		Set<A> availableActions = af.actions( s );
		Map<A, Double> normalisedUtils = freshUtilitiesFor( availableActions );
		double total = 0;
		A nextAction = null;
		for ( A action : availableActions ) {
			double normUtil = utilityTable.get( s ).get( action ) - minUtil;
			normalisedUtils.put( action, normUtil );
			total += normUtil;
		}
		double p = Math.random();
		double cumulativeP = 0.0;
		for ( Map.Entry<A, Double> actUtil : normalisedUtils.entrySet() ) {
			cumulativeP += actUtil.getValue() / total;
			if ( p <= cumulativeP ) {
				nextAction = actUtil.getKey();
				break;
			}
		}
		return nextAction;
	}
	
	// ACCOMODATE MORE STATES
	
	/*
	 * The original threshold value, to track adjustments while accommodating novel states. 
	 */
	private double originalThreshold;
	
	/*
	 * A default value to use when adjusting the hamming distance threshold to
	 * accommodate more states.
	 */
	private static final double DEFAULT_THRESHOLD_INCREMENT = 0.001;
	/*
	 * A default upper bound on the amount of positive adjustment to
	 * original hamming distance threshold.
	 */
	private static final double DEFAULT_MAX_ADJUSTMENT = 0.005;
	
	/**
	 * Allow a novel state to be learned when maximum size of internal state space has been reached.
	 * <p>A successful execution of this method should accommodate the {@code strangeState} 
	 * into the state space. This can include a state replacement policy, or adjusting the
	 * scheme of grouping similar states.</p>
	 * <p>Take care to avoid any side effects of the execution of this method that invalidate
	 * the learning already done. For example by causing previously discovered
	 * states that were similar to lose similarity.</p>
	 * @param strangeState The state that was discovered and found to be different from all
	 * states already explored, before the call to this method.
	 * @throws Exception if {@code strangeState} cannot be accommodated under application constraints,
	 * at the discretion of the implementer.
	 * @version 1
	 */
	@SuppressWarnings("unused")
	private void accommodateState( S strangeState ) throws Exception {
		/* Increment the hamming distance threshold to
		 * group together more states.
		 */
		double increment = getThresholdIncrement();
	    do {
	    	if ( threshold == originalThreshold + DEFAULT_MAX_ADJUSTMENT ) {
	    		throw new Exception( "Deviated too much from original hamming threshold." );
	    	}
	    	threshold += increment;
	    } while ( aggregateState( strangeState ) == null );
	}
	
	/*
	 * @return A small value used to increment the hamming distance threshold to
	 * accomodate new states.
	 */
	private double getThresholdIncrement() {
		return DEFAULT_THRESHOLD_INCREMENT;
	}
	
	public String utilityTable() {
		String result = "State,";
		Set<S> states = utilityTable.keySet();
		if ( s != null )
		    for ( A action : af.actions( s ) ) {
		    	String head = String.format( "%7s, ", action.toString() );
			    result += head;
		    }
		result += "\n";
		for ( S state : states ) {
			result += state + ", ";
			for ( A action : af.actions( state ) ) {
				double utility = utilityTable.get( state ).get( action );
				String entry = String.format( "%+.3f", utility );
				result += entry + ", ";
			}
			result += "\n";
		}
	    return result.trim();
	}
	
	public void reset() {
		this.initialiseUtilityTable();
		this.s = null;
		this.a = null;
		this.aggregateS = null;
	}
}
