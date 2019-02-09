package research.analysis;

import java.util.*;
import java.lang.*;

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
                def = new LocalDefinition(variable, new ConditionList()/*((ConditionList)src.get(_curr)).clone()*/, defStmt.getRightOp());
            }
            out.put(variable, def);
        } 

        for (Iterator<Map<Local, Definition>> it = fallOut.iterator(); it.hasNext(); )
            copy(out, it.next());

        for (Iterator<Map<Local, Definition>> it = branchOuts.iterator(); it.hasNext(); )
            copy(outBranch, it.next());
    }

	/**
	 * Generates SMT formula whose model represents parameter values which lead to execution of given Unit.
	 * @param unit Block to reach
	 */
	public StringBuilder resolveCondition(Unit unit) {
		Map<Local, Definition> currDefs = getFlowBefore(unit);
		ConditionList currConditions = (ConditionList)currDefs.get(_curr);
		debug("Conditions to match: " + currConditions.toString(), 3);
		Set<Implies> finalFormulas = new HashSet<Implies>();
		ConditionResolver resolver = new ConditionResolver(currDefs, _settings);
		for (Value cond : currConditions.getConditions()) {
			finalFormulas.addAll(resolver.resolve(cond));
		}

		debug("Final generated formulas (size " + finalFormulas.size() + "):", 3);
		if(finalFormulas.size() <= _settings.numOfFormulasToPrint) {
			for (Implies i : finalFormulas) {
				debug(i.toString(), 3);
			}
		}

		Printer printer = _settings.getPrinter();
		StringBuilder builder = new StringBuilder();
		Map<String, Integer> paramToIndex = mapParameters(currDefs);

		printer.print(builder, finalFormulas);

		builder.append("Parameters:\n");
		for(Map.Entry<String, Integer> entry : paramToIndex.entrySet()) {
			builder.append(entry.getKey() + ":" + entry.getValue().toString() + "\n");
		}
		return builder;
	}

	private Map<String, Integer> mapParameters(Map<Local, Definition> currDefs) {
		Map<String, Integer> paramToIndex = new HashMap<String, Integer>();
		for(Map.Entry<Local, Definition> entry : currDefs.entrySet()) {
			if(entry.getValue() instanceof LocalDefinition) {
				LocalDefinition ld = (LocalDefinition)entry.getValue();
				Map.Entry<Set<Value>, Value> def = ld.getDefinitions().entrySet().iterator().next();
				if(def.getKey().isEmpty() && def.getValue() instanceof ParameterRef)
					paramToIndex.put(entry.getKey().getName(), ((ParameterRef)def.getValue()).getIndex());
			}
		}
		return paramToIndex;
	}
}