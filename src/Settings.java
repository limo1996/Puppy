package research.analysis;

import java.io.*;
import soot.G;

/**
 * Settings of analysis.
 */
public class Settings {

	/**
	 * Enum representing current soot internal representation.
	 */
	public enum Representation {
		JIMPLE,
		SHIMPLE,
	}

	/**
	 * Enum representing user chosen backend SMT Solver.
	 */
	public enum SMTSolver {
		Z3
	}

    private int debug;
	private boolean testing;
	private Representation representation;
    private String test_out;
	private FileWriter stream;
	private SMTSolver solver;

	public int numOfFormulasToPrint = 20;

    /**
     * Creates new instance of Settings.
     * @param debug : debug level <1,3> -> the higher the less information (but more important) is printed.
     * @param test_out : path to output file for test purposes. If null no testing.
	 * @param repr : internal soot representation.
     */
    public Settings(int debug, String test_out, Representation repr, SMTSolver solver){
        this.debug = debug;
        this.testing = test_out != null;
		this.test_out = test_out;
		this.representation = repr;
		this.solver = solver;

		testStart();
    }

    /** 
     * Debug output to console.
     */
    public void debug(String msg, int level, boolean newLine) {
        if(level >= debug) {
            G.v().out.print(newLine ? msg + "\n" : msg);
        }
    }

    /** 
     * Test output to file specified in contructor. Nothing happens if file path was null.
     */
    public void test(String msg, boolean newLine) {
        if(testing){
            assert stream != null : "Stream null but testing?";
            try {
                stream.write(newLine ? msg + "\n" : msg);
            } catch (Exception ex) {
                debug(ex.toString(), 3, true);
            }
        }
	}
	
	/**
	 * Starts tests i.e. creates new file stream..
	 */
	public void testStart() {
		if(testing){
            try {
                File file = new File(test_out);
                file.createNewFile();
                new PrintWriter(file).close();
                stream = new FileWriter(file, true);
            } catch (Exception ex) {
                debug(ex.toString(), 3, true);
            }
        }
	}

    /**
     * Should be called at the end of the test. Flushes the data, closes the stream.
     */
    public void testEnd() {
        if(stream != null) {
            try {
                stream.flush();
                stream.close();
            } catch (Exception ex) {
                debug(ex.toString(), 3, true);
            }
        }
	}
	
	/**
	 * Returns current Soot internal representation.
	 */
	public Representation getRepresentation() {
		return this.representation;
	}

	/**
	 * Gets print according to user defined solver.
	 * @return
	 */
	public Printer getPrinter() {
		switch(solver) {
			case Z3: return new Z3Printer();
		}
		return null;
	}
}