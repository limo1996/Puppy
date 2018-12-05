package research.analysis;

import soot.*;
import soot.jimple.*;

/**
 * Replaces locals in given expression with given value.
 */
public class LocalReplacer extends AbstractBaseSwitch {
	private String target;
	private Value with;
	private ConditionResolver resolver;

	/**
	 * Creates new instance of LocalReplacer.
	 * @param resolver used for debug output.
	 */
	public LocalReplacer(ConditionResolver resolver) {
		this.resolver = resolver;
	}

	/**
	* Replaces *target* in given *expr* *with* a given value.
	*/
	public void replace(Local target, Value with, Value expr) {
		if(with.toString().equals(target.getName())){
			resolver.debug("Replacing " + target.toString() + " with itself in " + expr.toString() + ". Skipping it!", 2);
			return;
		}
		resolver.debug("Replacing " + target.toString() + " with " + with.toString() + " in " + expr.toString(), 1);
		this.target = target.getName();
		this.with = with;
		defaultCase(expr);
	}

	public void unopExpr(UnopExpr v) { 
		if(tryReplace(v.getOpBox()))
			defaultCase((RLocal)v.getOp()); 
	}

	public void numericConstant(NumericConstant v) {}

	/** 
	 * Only RLocal can reach this callback since Locals are handles one level up.
	 * If RLocal is replaced by target than we change replacedBy otherwise we 
	 * traverse deeper.
	 */
	public void caseLocal(Local v) {
		RLocal sl = (RLocal)v;
		if (sl.getReplacedBy() instanceof Local && !(sl.getReplacedBy() instanceof RLocal)) {
			Local l = (Local)sl.getReplacedBy();
			if (l.getName().equals(target)) {
				sl.setReplacedBy(with);
			}
		} else
			defaultCase(((RLocal)v).getReplacedBy());
	}

	public void binopExpr(BinopExpr v) {
		ValueBox vb1 = v.getOp1Box(), vb2 = v.getOp2Box();
		if (tryReplace(vb1)) 
			defaultCase(vb1.getValue());
		
		if (tryReplace(vb2))
			defaultCase(vb2.getValue());
	}

	// If value is RLocal than returns true and does nothing.
	// Otherwise checks if value is local and if its target.
	// In case it is it replaces it otherwise does nothing.
	// Returns true if value is RLocal otherwise false.
	private boolean tryReplace(ValueBox vb) {
		Value v = vb.getValue();
		boolean b = v instanceof RLocal;
		if (v instanceof RLocal)
			return true;
		if (v instanceof Local) {
			Local l = (Local)v;
			if (l.getName().equals(target)) {
				vb.setValue(new RLocal(l, with));
			}
		}
		return false;
	}
}