package research.analysis;

import soot.*;
import soot.jimple.*;
import java.util.*;


/**
 * Searches given expression for first non parameter expression.
 */
public class NParamFinder extends AbstractBaseSwitch {
	private Map<String, Definition> definitions;
	private Local firstNotParam;
	private ConditionResolver resolver;

	/**
	 * Creates new instance of NParamFinder. Definitions are used for checking if variable is parameter.
	 */
	public NParamFinder(Map<String, Definition> definitions, ConditionResolver resolver) {
		this.definitions = definitions;
		this.resolver = resolver;
	}

	/**
	 * Finds first local that is not parameter.
	 * @param expr to search in.
	 * @return local that is not param or null.
	 */
	public Local findFirstNotParam(Value expr) {
		firstNotParam = null;
		defaultCase(expr);
		resolver.debug("NPARAMFINDER return " + firstNotParam + " for " + expr.toString(), 1);
		return firstNotParam;
	}

	public void unopExpr(UnopExpr v) { defaultCase(v.getOp()); }
	public void numericConstant(NumericConstant v) {}

	// **** VARIABLE ****
	// Check if is RLocal -> continue traversing
	// else its a leaf node and therefore do the checking
	public void caseLocal(Local v) {
		if(v instanceof RLocal)
			defaultCase(((RLocal)v).getReplacedBy());
		else if(!isParam(v) && firstNotParam == null)
			firstNotParam = v;
	}

	// returns true if var is parameter
	private boolean isParam(Local var) {
		LocalDefinition ld = (LocalDefinition)definitions.get(var.getName());
		Map.Entry<Set<Value>, Value> def = ld.getDefinitions().entrySet().iterator().next();
		return def.getKey().isEmpty() && def.getValue() instanceof ParameterRef;
	}

	public void binopExpr(BinopExpr v) {
		defaultCase(v.getOp1());
		defaultCase(v.getOp2());
	}
}