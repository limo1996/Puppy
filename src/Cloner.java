package research.analysis;

import soot.*;
import soot.jimple.*;

public class Cloner extends AbstractBaseSwitch {
	private Value result;

	/**
	* Replaces *target* in given *expr* *with* a given value.
	*/
	public Value clone(Value v) {
		defaultCase(v);
		return result;
	}

	public void unopExpr(UnopExpr v) { 
		defaultCase(v.getOp());
		UnopExpr n = (UnopExpr)v.clone();
		n.getOpBox().setValue(result);
		result = n;
	}

	public void numericConstant(NumericConstant v) { result = v; }

	/** 
	 * Only RLocal can reach this callback since Locals are handles one level up.
	 * If RLocal is replaced by target than we change replacedBy otherwise we 
	 * traverse deeper.
	 */
	public void caseLocal(Local v) {
		if(v instanceof RLocal) {
			defaultCase(((RLocal)v).getReplacedBy());
			RLocal n = (RLocal)v.clone();
			n.setReplacedBy(result);
			result = n;
		} else {
			result = v;
		}
	}

	public void binopExpr(BinopExpr v) {
		defaultCase(v.getOp1Box().getValue());
		Value left = result;
		defaultCase(v.getOp2Box().getValue());
		Value right = result;
		BinopExpr n = (BinopExpr)v.clone();
		n.getOp1Box().setValue(left);
		n.getOp2Box().setValue(right);
		result = n;
	}
}