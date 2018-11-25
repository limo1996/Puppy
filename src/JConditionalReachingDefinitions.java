package research.analysis;

import java.util.*;

import research.analysis.ConditionalReachingDefinitions;
import soot.*;
import soot.Value;
import soot.jimple.*;
import soot.shimple.*;
import soot.jimple.internal.*;
import soot.options.*;
import soot.toolkits.graph.*;
import soot.toolkits.scalar.*;

/**
 * Class that runs Conditional Definitions Analysis on UnitGraph of provided method with Jimple internal representation.
 */
public class JConditionalReachingDefinitions extends ConditionalReachingDefinitions {

    /**
     * Creates new instance of ReachingDefinitions + runs conditional definitions analysis on Jimple representation.
     * Prints debug and test output if specified in settings.
     */
    JConditionalReachingDefinitions(UnitGraph graph, Settings settings) {
        super(graph, settings);

        Body b = graph.getBody();

        // Create entry flow by assigning default values to all locals.
        for (Local l : b.getLocals()) {
            Value defaultV = Utils.getDefaultValue((JimpleLocal)l);
            Definition def = new LocalDefinition(l, new ConditionList(), defaultV);
            _entryFlow.put(l, def);
        }

        doAnalysis();
        print();
    }

    /**
     * Merging two maps consists of joining the lattice values of every
     * variable contained in those maps
     */
	@Override
    protected void merge(Map<Local, Definition> src1,
                         Map<Local, Definition> src2,
                         Map<Local, Definition> dest) {
        // TODO: Check if deep copies are needed

        // adds negated conditions to variables modified only in one branch
        // see documentation of compensateFalledOut function for more details
        compensateFalledOut(src1, src2);
        compensateFalledOut(src2, src1);

		super.merge(src1, src2, dest);
    }

    /**
     * In our analysis we are interested in two cases:
     * If statement: guards defintion by condition
     * Definition: Defines new Value of local variable (Can be guarded by condition i.e. if inside if stmt)
     */
    protected void flowThrough(Map<Local, Definition> src, Unit unit,
                               List<Map<Local, Definition>> fallOut, // fallout -> branch that follows not satisfied condition
                               List<Map<Local, Definition>> branchOuts // branchout -> branch that follows satisfied condition
                               ) {
        Stmt s = (Stmt) unit;
        Map<Local, Definition> out = new HashMap<Local, Definition>(src);
        Map<Local, Definition> outBranch = new HashMap<Local, Definition>(src);

        if (s instanceof IfStmt) {
            ConditionExpr condition = (ConditionExpr)((IfStmt)s).getCondition();
            ConditionList list = ((ConditionList)outBranch.get(_curr)).clone();     // need to duplicate curr conditions to be consistent
            ((ConditionList)outBranch.get(_curr)).add(condition);
            list.add(Utils.negate(condition));
            out.put(_curr, list);
            debug("Fork " + outBranch.toString() + " with " + out.toString(), 1);
        } else if (s instanceof DefinitionStmt) {
			DefinitionStmt defStmt = (DefinitionStmt) s;
			if(defStmt.getLeftOp() instanceof Local) {
            	Local variable = (Local)defStmt.getLeftOp();
            	Value definition = defStmt.getRightOp();
            	LocalDefinition def = new LocalDefinition(variable, ((ConditionList)src.get(_curr)).clone(), defStmt.getRightOp());
				out.put(variable, def);
			}
        } 

        for (Iterator<Map<Local, Definition>> it = fallOut.iterator(); it.hasNext(); )
            copy(out, it.next());

        for (Iterator<Map<Local, Definition>> it = branchOuts.iterator(); it.hasNext(); )
            copy(outBranch, it.next());
    }

    /**
     * In case of if statements without else block or ifs that do not modify same set of variables in both branches
     * we need to modify definitions of these variables in order to be conditant. 
     * 
     * This function compensates definitions of these variables. It adds these missing conditions
     * to definitions int falledout map
     * This function should be called twice with switched parameters.
     *
     * Example:
     * int i = 0;
     * if(x == 0) {
     *      i = 1;
     * }
     *
     * Explanation:
     * Definition of i should be |x==0 ==> i=1 and x!=0 ==> i=0| but without calling this function is:
     * |x==0 ==> i=1 and i=0| because we did not modify definition in falledout branch.
     */
    protected void compensateFalledOut(Map<Local, Definition> branched, Map<Local, Definition> falledout) {
        // find condition that branched has taken
        Set<Value> blist = ((ConditionList)branched.get(_curr)).getConditions();
        Set<Value> flist = ((ConditionList)falledout.get(_curr)).getConditions();
        Value condition = findCondition(blist, flist);
        Value negCondition = findCondition(flist, blist);

        debug("Condition: " + condition, 2);

        // find variables that were modified by one branch but not the other
        Set<Local> modifiedLocals = new HashSet<Local>();
        for(Map.Entry<Local, Definition> entry : branched.entrySet()) {
            // if variable was modified in one branch but not in the other then we add it
            debug("Local: " + entry.getKey() + ", definition: " + entry.getValue() + " " 
            + entry.getValue().containsCondition(condition) + " " + falledout.get(entry.getKey()).containsCondition(negCondition), 1);
            if(entry.getValue() instanceof LocalDefinition && entry.getValue().containsCondition(condition)
               && !falledout.get(entry.getKey()).containsCondition(negCondition)) {  
                modifiedLocals.add(entry.getKey());
            }
        }

        debug("Modified locals: " + modifiedLocals, 2);

        // add condition of falled out branch (negated condition of branched out branch)
        // to variables that were modified in branch out branch but not by falled out
        for(Map.Entry<Local, Definition> entry : falledout.entrySet()) {
            if(modifiedLocals.contains(entry.getKey())) {
                LocalDefinition ldef = (LocalDefinition)entry.getValue();
                for(Set<Value> key : ldef.getDefinitions().keySet()) {
                    key.add(negCondition);
                }
            }
        }
    }

    // finds condition that set1 contains but set2 not. Must be only one.
    private Value findCondition(Set<Value> set1, Set<Value> set2) {
        Value condition = null;
        for(Value cond : set1) {
            if(!set2.contains(cond)) {
                assert (cond == null) : "This should never happen. Branches differ in more than one condition.";
                condition = cond;
            }
        }

        assert (condition != null) : "This should never happen. Branches have to differ in one condition.";
        return condition;
    }

    public StringBuilder resolveCondition(Unit unit) { return null; }
}