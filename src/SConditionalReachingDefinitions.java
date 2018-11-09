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
 * Class that runs Conditional Definitions Analysis on UnitGraph of provided method with Shimple internal representation.
 */
public class SConditionalReachingDefinitions extends ConditionalReachingDefinitions {

    /**
     * Creates new instance of ReachingDefinitions + runs conditional definitions analysis on Shimple representation.
     * Prints debug and test output if specified in settings.
     */
    SConditionalReachingDefinitions(UnitGraph graph, Settings settings) {
        super(graph, settings);
        doAnalysis();
        print();
    }

    /**
     * In our analysis we are interested in two cases:
     * If statement: guards defintion by condition
     * Definition: Defines new Value of local variable (Can be guarded by condition i.e. if inside if stmt)
	 * 		'---> special case Phi definition. Need to merge definitions into new one.
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
            Local variable = (Local)defStmt.getLeftOp();
            Value definition = defStmt.getRightOp();
            LocalDefinition def = null;
            if (definition instanceof PhiExpr) {
				// in case of Phi definition we need to merge existing definitions into new one.
				// Therefore we iterate through definitions that need to be merged and take condition
				// list from their branch. It represents conditional state so we take it as 
				// condition guard into new definition and as value we simple pick variable of curr. def.
                debug(definition.toString(), 1);
                PhiExpr expr = (PhiExpr)definition;
                def = new LocalDefinition(variable);
                for (int i = 0; i < expr.getArgCount(); i++) {
                    Local local = (Local)expr.getValue(i);
                    ConditionList conditions = (ConditionList)getFlowBefore(expr.getPred(i)).get(_curr);
                    def.addDefinition(conditions.getConditions(), local);
                }
            } else {
                def = new LocalDefinition(variable, ((ConditionList)src.get(_curr)).clone(), defStmt.getRightOp());
            }
            out.put(variable, def);
        } 

        for (Iterator<Map<Local, Definition>> it = fallOut.iterator(); it.hasNext(); )
            copy(out, it.next());

        for (Iterator<Map<Local, Definition>> it = branchOuts.iterator(); it.hasNext(); )
            copy(outBranch, it.next());
    }

    public Value resolveCondition(Unit unit, ConditionExpr condition) {
        assert condition != null : "ResolveCondition: condition cannot be null!";

        ValueBox vb1 = condition.getOp1Box();
        ValueBox vb2 = condition.getOp2Box();

        return null;
    }

    private Value resolveValue(){
        return null;
    }
}