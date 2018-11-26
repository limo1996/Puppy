package research.analysis;

import soot.*;
import soot.jimple.*;

/**
 * Base class for our visitors which simplifies AST traversal by grouping expressions into 
 * 4 groups: Binary expressions (binopExpr), Unary expressions (unopExpr), 
 * Numeric Constants (numericConstant) and Locals (caseLocal).
 */
public abstract class AbstractBaseSwitch extends AbstractJimpleValueSwitch {
	// **** LOGICAL EXPRS ****
	public void caseAndExpr(AndExpr v) { binopExpr(v); }
	public void caseOrExpr(OrExpr v) { binopExpr(v); }
	public void caseNegExpr(NegExpr v) { unopExpr(v); }

	// **** COMPARISON EXPRS ****
	public void caseCmpExpr(CmpExpr v) { 
		G.v().out.println("Cmp expr encountered: " + v.toString());
		binopExpr(v);
	}

	public void caseCmpgExpr(CmpgExpr v) { // >
		G.v().out.println("Cmpg expr encountered: " + v.toString());
		binopExpr(v);
	}

	public void caseCmplExpr(CmplExpr v) { // <
		G.v().out.println("Cmpl expr encountered: " + v.toString());
		binopExpr(v);
	}

	public void caseGtExpr(GtExpr v) { binopExpr(v); }
	public void caseGeExpr(GeExpr v) { binopExpr(v); }
	public void caseLtExpr(LtExpr v) { binopExpr(v); }
	public void caseLeExpr(LeExpr v) { binopExpr(v); }
	public void caseEqExpr(EqExpr v) { binopExpr(v); }
	public void caseNeExpr(NeExpr v) { binopExpr(v); }

	// **** NUMBER ARITHMETICS ****
	public void caseDivExpr(DivExpr v) { binopExpr(v); }
	public void caseMulExpr(MulExpr v) { binopExpr(v); }
	public void caseSubExpr(SubExpr v) { binopExpr(v); }
	public void caseAddExpr(AddExpr v) { binopExpr(v); }

	public void caseParameterRef(ParameterRef v) { }

	// **** CONSTANTS ****
	public void caseDoubleConstant(DoubleConstant v) { numericConstant(v); }
	public void caseFloatConstant(FloatConstant v) { numericConstant(v); }
	public void caseIntConstant(IntConstant v) { numericConstant(v); }
	public void caseLongConstant(LongConstant v) { numericConstant(v); }
	public void caseNullConstant(NullConstant v) {
		assert false : "NullConstant not accepted";
	}

	// **** HELPER METHODS ****
	public void defaultCase(Object v) {
		((Value)v).apply(this);
	}

	public abstract void binopExpr(BinopExpr v);
	public abstract void unopExpr(UnopExpr v);
	public abstract void numericConstant(NumericConstant c);
}