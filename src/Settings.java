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
    private String test_out, timing_out;
	private FileWriter stream, time_stream;
	private SMTSolver solver;
	private int n_of_runs;

	public int numOfFormulasToPrint = 20;

    /**
     * Creates new instance of Settings.
     * @param debug : debug level <1,3> -> the higher the less information (but more important) is printed.
     * @param test_out : path to output file for test purposes. If null no testing.
	 * @param repr : internal soot representation.
     */
    public Settings(int debug, String test_out, Representation repr, SMTSolver solver, int runs, String timing_out){
        this.debug = debug;
        this.testing = test_out != null;
		this.test_out = test_out;
		this.representation = repr;
		this.solver = solver;
		this.n_of_runs = runs;
		this.timing_out = timing_out;
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
	
	private long startTime;
	public void stopwatchStart() {
		startTime = System.nanoTime();
	}

	public void stopwatchEnd(String method) {
		long estimatedTime = System.nanoTime() - startTime;
		double elapsedTime = (double)estimatedTime / 1_000_000_000.0;
		try {
			time_stream.write(method + ":" + elapsedTime + "\n");
		} catch (Exception ex) {
			debug(ex.toString(), 3, true);
		}
	}

	/**
	 * @return number of times program should run
	 */
	public int getNumberOfRuns() {
		return n_of_runs;
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
				stream = createFileWriter(this.test_out);
				time_stream = createFileWriter(this.timing_out);
            } catch (Exception ex) {
                debug(ex.toString(), 3, true);
            }
        }
	}

	private FileWriter createFileWriter(String fname) throws FileNotFoundException, IOException {
		File file = new File(fname);
        file.createNewFile();
        //new PrintWriter(file).close();
        return new FileWriter(file, true);
	}

    /**
     * Should be called at the end of the test. Flushes the data, closes the stream.
     */
    public void testEnd() {
        if(stream != null) {
            try {
                stream.flush();
				stream.close();
				time_stream.flush();
				time_stream.close();
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