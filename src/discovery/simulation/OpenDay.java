package discovery.simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import rl.agent.RLProgram;

import discovery.action.DiscoveryActionsFunction;
import discovery.action.DiscoveryDutyCycle;
import discovery.action.DutyCyclePriority;
import discovery.agent.NodeAgent;
import discovery.agent.DiscoveryEPF;
import discovery.agent.state.DiscoveryPercept;
import discovery.environment.PedestrianEnvironment;
import discovery.environment.ContactAreaState;
import discovery.environment.StateFactory;

public class OpenDay {
	
	private static final String PATTERN_FILE = "bin" + File.separator +
                                                  "discovery" + File.separator + 
                                                  "simulation" + File.separator + "arrivalPattern.csv";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        File patternFile = new File( PATTERN_FILE );
        try {
			BufferedReader patternReader = new BufferedReader( new FileReader( patternFile ) );
			String[] times = patternReader.readLine().split( "," );
			String[] activityLevels = patternReader.readLine().split( "," );
			ContactAreaState contactArea = StateFactory.createHourlyPattern( times, activityLevels );
			//ContactAreaState contactArea = StateFactory.dailyPattern();
			
			DiscoveryActionsFunction af = new DiscoveryActionsFunction();
		    DutyCyclePriority pr = new DutyCyclePriority();
		    
		    int maxNumStates = 12;
		    double threshold = 5.0;
		    
		    DiscoveryEPF epf = null; //new DiscoveryEPF( 10000 );
		    
		    double maxProbExplore = 0.3;
		    double minProbExplore = 0.02;
		    double learningRate = 0.5;
		    double discount = 0;
		    
		    RLProgram<DiscoveryPercept, DiscoveryDutyCycle> program;
		    program = new RLProgram<DiscoveryPercept, DiscoveryDutyCycle>( af, pr, maxNumStates, 
		    		                                                         threshold, epf, maxProbExplore,
		    		                                                         minProbExplore, learningRate, discount );
		    
		    NodeAgent agent;
		    agent = new NodeAgent( program, 864000 );
		    
		    PedestrianEnvironment trial;
		    trial = new PedestrianEnvironment( agent, contactArea );
		    trial.executeTrial();
		    
		} catch (FileNotFoundException e) {
			System.err.println( "Could not find input file." );
		} catch (IOException e) {
			System.err.println( "Problem reading from the file." );
		}
	}

}
