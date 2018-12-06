package research.analysis;

import java.util.*;

import soot.*;
import soot.Value;
import soot.jimple.*;
import soot.shimple.*;
import soot.jimple.internal.*;
import soot.options.*;
import soot.toolkits.graph.*;
import soot.toolkits.scalar.*;

/**
 * Class that runs Conditional Definitions Analysis on UnitGraph of provided method.
 */
public abstract class ConditionalReachingDefinitions extends ForwardBranchedFlowAnalysis<Map<Local, Definition>> {
    protected Map<Local, Definition> _entryFlow;
    protected JimpleLocal _curr;
    protected UnitGraph _graph;
    protected Settings _settings;

    /**
     * Creates new instance of ReachingDefinitions + runs conditional definitions analysis.
     * Prints debug and test output if specified in settings.
     */
    public ConditionalReachingDefinitions(UnitGraph graph, Settings settings) {
        super(graph);

        _entryFlow = new HashMap<Local, Definition>();
        _graph = graph;
        _settings = settings;
        _curr = new JimpleLocal("_hopefully_some_impossible_variable_name_6163361", BooleanType.v());

        _entryFlow.put(_curr, new ConditionList());

        debug("'-----------> Analyzing " + graph.getBody().getMethod().getName(), 3);
        test("method:" + graph.getBody().getMethod().getName());
    }

    /**
     * Prints IN sets for every statement + outputs IN set of return stmt 
     * in test format to given test_out file for correctness testing.
     */
    public void print() {
        Iterator unitIt = graph.iterator();
        while(unitIt.hasNext()){
            Unit s = (Unit) unitIt.next();
            Map<Local, Definition> set = getFlowBefore(s);
            int level = 2;
            if(s.toString().trim().equals("return"))
                level = 3;

            debug("Unit: " + s.toString(), level);
            for(Map.Entry<Local, Definition> entry : set.entrySet()){
                String output = entry.getKey().getName() + ":" + entry.getValue().toString();
                debug(output, level);
            }
        }
	}
	
	/**
	 * Returns block which contains target.
	 * @param target to contain
	 * @return Unit
	 */
	public Unit getTargetBlock(String target) {
		Iterator unitIt = graph.iterator();
		while(unitIt.hasNext()){
			Unit s = (Unit) unitIt.next();
			if(s.toString().contains(target))
				return s;
		}
		return null;
	}

    /**
     * Copy simply copies the source to the destination.
     */
    protected void copy(Map<Local, Definition> src, Map<Local, Definition> dest) {
        //dest.clear();
        dest.putAll(src);
        for(Map.Entry<Local, Definition> entry: src.entrySet()){
            assert (entry.getValue() == dest.get(entry.getKey()));
        }
    }

    /**
     * Merging two maps consists of joining the lattice values of every
     * variable contained in those maps.
     */
    protected void merge(Map<Local, Definition> src1,
                         Map<Local, Definition> src2,
                         Map<Local, Definition> dest) {
		// merge two branching by joining values one the same keys and adding values on distinct keys. 
        copy(src1, dest);

        for (Map.Entry<Local, Definition> entry : src2.entrySet()) {
            Local key = entry.getKey();
            Definition value = entry.getValue();
            if (dest.containsKey(key)) {
                // join definitions if present in both lists
                dest.put(key, value.join(dest.get(key)));
            } else {
                dest.put(key, value);
            }
		}
		debug("Merging " + src1.toString() + " with " + src2.toString() + " to " + dest.toString(), 1);
		//debug("Merging " + src1.get(_curr).toString() + " with " + src2.get(_curr).toString() + " to " + dest.get(_curr).toString(), 1);
    }

    /**
     * Initial flow to entry block of the CFG.
     */
    protected Map<Local, Definition> entryInitialFlow() {
        return new HashMap<Local, Definition>(_entryFlow);
    }

    /**
     * Initial flow to every block of the CFG.
     */
    protected Map<Local, Definition> newInitialFlow() {
        return new HashMap<Local, Definition>();
    }

	/**
	 * Returns condition list for the unit.
	 * @param unit block in program for which conditions we are interested in.
	 * @return ConditionList of the unit.
	 */
    public ConditionList getUnitConditionList(Unit unit) {
		return (ConditionList)getFlowBefore(unit).get(_curr);
	}

	public abstract StringBuilder resolveCondition(Unit unit);

    protected void debug(String msg){
        debug(msg, 1);
    }

    protected void debug(String msg, int debug){
        debug(msg, debug, true);
    }

    // debug output
    protected void debug(String msg, int level, boolean newLine) {
        _settings.debug(msg, level, newLine);
    }

    protected void test(String msg) {
        test(msg, true);
    }

    // appends to output file which will be tested
    protected void test(String msg, boolean newLine) {
        _settings.test(msg, newLine);
    }
}