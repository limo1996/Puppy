package research.analysis;

import java.util.*;
import java.io.*;
import soot.*;
import soot.toolkits.graph.*;
import soot.toolkits.scalar.FlowSet;

/**
 * Class that runs our analysis on every method with settings obtained through command line.
 */
public class AnalysisTransformer extends SceneTransformer {
    String test_out;

    public AnalysisTransformer(String test_out) {
        this.test_out = test_out;
    }

	@Override
	protected void internalTransform(String arg0, Map arg1) {
		Settings s = new Settings(3, test_out);
        
        // for every method in the class
		for (SootMethod sMethod : Scene.v().getMainClass().getMethods()) {
            try {
		        // Create graph based on the method
		        UnitGraph graph = new BriefUnitGraph(sMethod.retrieveActiveBody());

		        // Perform Data Flow Analysis on the Graph
		        ReachingDefinitions analysis = new ReachingDefinitions(graph, s);

            } catch(RuntimeException ex) {
                G.v().out.println(ex.getMessage());
            }
        }
        s.testEnd();
	}
}