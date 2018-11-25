package research.analysis;

import soot.*;
import soot.jimple.*;

public class LocalReplacer extends AbstractBaseSwitch {
	private String target;
	private Value with;

	public void replace(Local target, Value with, Value expr) {
		assert with.toString() != target.getName() : "Thats funcked up";
		//G.v().out.println("Replace " + target.toString() + " with " + with.toString() + " in " + expr.toString());
		this.target = target.getName();
		this.with = with;
		defaultCase(expr);
	}

	public void unopExpr(UnopExpr v) { 
		if(tryReplace(v.getOpBox()))
			defaultCase((RLocal)v.getOp()); 
	}

	public void numericConstant(NumericConstant v) {}

	public void caseLocal(Local v) {
		if (v instanceof RLocal) {
			RLocal sl = (RLocal)v;
			if (sl.getReplacedBy() instanceof Local && !(sl.getReplacedBy() instanceof RLocal)) {
				Local l = (Local)sl.getReplacedBy();
				//G.v().out.println(l.getName() + " = " + target);
				if (l.getName().equals(target)) {
					sl.setReplacedBy(new RLocal(l, with));
					//G.v().out.println("Replacing " + sl.getReplacedBy().toString() + " with RLocal");
				}
			} else
				defaultCase(((RLocal)v).getReplacedBy());
		}
	}

	public void binopExpr(BinopExpr v) {
		//G.v().out.println(v.toString());
		ValueBox vb1 = v.getOp1Box(), vb2 = v.getOp2Box();
		if (tryReplace(vb1)) 
			defaultCase(vb1.getValue());
		
		if (tryReplace(vb2))
			defaultCase(vb2.getValue());
			
	}

	private boolean tryReplace(ValueBox vb) {
		Value v = vb.getValue();
		boolean b = v instanceof RLocal;
		if (v instanceof RLocal)//TODO:
			return true;
		if (v instanceof Local) {
			Local l = (Local)v;
			//G.v().out.println(l.getName() + " = " + target);
			if (l.getName().equals(target)) {
				vb.setValue(new RLocal(l, with));
				//G.v().out.println("Replacing " + v.toString() + " with RLocal");
			}
		}
		return false;
	}
}