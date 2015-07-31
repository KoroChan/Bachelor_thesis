package discovery.simulation;


import rl.agent.RLProgram;
import discovery.action.DiscoveryActionsFunction;
import discovery.action.DutyCyclePriority;
import discovery.agent.NodeAgent;
import discovery.agent.DiscoveryEPF;
import discovery.agent.state.DiscoveryPercept;
import discovery.action.DiscoveryDutyCycle;
import discovery.environment.PedestrianEnvironment;
import discovery.environment.ContactAreaState;
import discovery.environment.StateFactory;

public class StandardExperiments {
	
	public static void main( String[] args ) {
	    DiscoveryActionsFunction af = new DiscoveryActionsFunction();
	    DutyCyclePriority pr = new DutyCyclePriority();
	    int maxNumStates = 12;
	    double threshold = 5.0;
	    DiscoveryEPF epf = new DiscoveryEPF( 4704 );
	    double maxProbExplore = 0.3;
	    double minProbExplore = 0.1;
	    double learningRate = 0.5;
	    double discount = 0;
	    RLProgram<DiscoveryPercept, DiscoveryDutyCycle> program;
	    program = new RLProgram<DiscoveryPercept, DiscoveryDutyCycle>( af, pr, maxNumStates, 
	    		                                                         threshold, epf, maxProbExplore,
	    		                                                         minProbExplore, learningRate, discount );
	    ContactAreaState contactArea;
	    contactArea = StateFactory.hourlyPattern();
	    NodeAgent agent;
	    agent = new NodeAgent( program, 1209600 );
	    
	    PedestrianEnvironment trial;
	    trial = new PedestrianEnvironment( agent, contactArea );
	    trial.executeTrial();
	}
}
