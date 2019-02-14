package research.analysis;

import soot.*;

/**
 * Runs Data flow analysis with provided command line arguments.
 */
public class RunDataFlowAnalysis {
	public static void main(String[] args) {
		
        String test_out = null, wholep = "-w", jimple_s = "J", pack="wjtp", time_out = null;
		Settings.Representation representation = Settings.Representation.JIMPLE;
		int n_of_runs = 1;

		// parse command line arguments
		if (args.length == 0) {
			System.out.println("Usage: java research.analysis.RunDataFlowAnalysis class_to_analyse out_file");
			System.exit(1);
		} else {
            System.out.println("------- Analyzing class: " + args[0] + " ------------");
            if (args.length >= 2) {
                representation = args[1].equals("J") ? Settings.Representation.JIMPLE : Settings.Representation.SHIMPLE;
            }
            if (args.length >= 3) {
                test_out = args[2];
			}
			if(args.length >= 4) {
				assert args.length >= 5 : "Timing out file not provided"; 
				n_of_runs = Integer.parseInt(args[3]);
				time_out = args[4];
			}
        }
		
		// set parameters for shimple
        if (representation == Settings.Representation.SHIMPLE) {
            assert args[1].equals("S");
            wholep = "-ws";
            jimple_s = "S";
            pack = "wstp";
        }

		String mainClass = args[0];

		// You may have to update the class Path based on your OS and Java version 
		/*** *** YOU MAY EDIT THIS TO ADD THE PATH TO rt.jar (IF NEEDED) WHILE TESTING ON YOUR MACHINE *** ***/
		String classPath = "soot.jar:../test";
		

		//Set up arguments for Soot
		String[] sootArgs = {
			"-cp", classPath, "-pp", 	// sets the class path for Soot
			"-allow-phantom-refs",		// in case certain references are not correct (older versions of Java)
            wholep, 					// Whole program analysis, necessary for using Transformer **s added for shimple**
			"-src-prec", "c",			// Specify type of source file
			"-main-class", mainClass,	// Specify the main class 
            "-f", jimple_s, 			// Specify type of output file **added for shimple**
			mainClass 
		};

		// Create transformer for analysis
		AnalysisTransformer analysisTransformer = new AnalysisTransformer(new Settings(3, test_out, representation, Settings.SMTSolver.Z3, n_of_runs, time_out));

		// Add transformer to appropriate Pack in PackManager. PackManager will run all Packs when main function of Soot is called
		PackManager.v().getPack(pack).add(new Transform(pack + ".baf", analysisTransformer));

		// Call main function with arguments
		soot.Main.main(sootArgs);

	}
}
