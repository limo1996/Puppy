package research.analysis;

import soot.*;
import soot.jimple.*;
import java.util.*;


public class NParamFinder extends AbstractBaseSwitch {
	private Map<String, Definition> definitions;
	private Local firstNotParam;

	public NParamFinder(Map<String, Definition> definitions) {
		this.definitions = definitions;
	}

	/**
	 * Finds first local that is not parameter.
	 * @param expr to search in.
	 * @return local that is not param or null.
	 */
	public Local findFirstNotParam(Value expr) {
		//G.v().out.println("searching for " + expr.toString());
		firstNotParam = null;
		defaultCase(expr);
		//G.v().out.println("NPARAMFINDER return " + firstNotParam);
		return firstNotParam;
	}

	public void unopExpr(UnopExpr v) { defaultCase(v.getOp()); }
	public void numericConstant(NumericConstant v) {}

	// **** VARIABLES ****
	public void caseLocal(Local v) {
		if(v instanceof RLocal)
			defaultCase(((RLocal)v).getReplacedBy());
		else if(!isParam(v) && firstNotParam == null)
			firstNotParam = v;
	}

	private boolean isParam(Local var) {
		LocalDefinition ld = (LocalDefinition)definitions.get(var.getName());
		Map.Entry<Set<Value>, Value> def = ld.getDefinitions().entrySet().iterator().next();
		return def.getKey().isEmpty() && def.getValue() instanceof ParameterRef;
	}

	public void binopExpr(BinopExpr v) {
		//G.v().out.println(v.toString());
		defaultCase(v.getOp1());
		defaultCase(v.getOp2());
	}
}