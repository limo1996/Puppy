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
	Settings _s;	// User provided settings.

	/**
	 * Creates new instance of AnalysisTransformer.
	 * @param s : Settings parsed from cmd line
	 */
	public AnalysisTransformer(Settings s) {
		_s = s;
	}

	@Override
	protected void internalTransform(String arg0, Map arg1) {

		// for number of runs (performance measure)
		for(int r = 0; r < _s.getNumberOfRuns(); r++) {
			// for every method in the class
			for (SootMethod sMethod : Scene.v().getMainClass().getMethods()) {
				try {
					// Create graph based on the method
					UnitGraph graph = new BriefUnitGraph(sMethod.retrieveActiveBody());

					_s.stopwatchStart();

					// Perform Data Flow Analysis on the Graph
					ConditionalReachingDefinitions analysis = DefinitionFactory.getRepresentation(graph, _s);
					Unit errBlock = analysis.getTargetBlock("error");
					if(errBlock != null) {
						_s.test(analysis.resolveCondition(errBlock).toString(), true);
					} else {
						G.v().out.println("Method " + sMethod.getName() + " does not contain error method call. Skipping...");
					}

					_s.stopwatchEnd(sMethod.getName());
				} catch(RuntimeException ex) {
					G.v().out.println(ex.getMessage());
					ex.printStackTrace(G.v().out);
				}
			}
		}
		_s.testEnd();
	}
}