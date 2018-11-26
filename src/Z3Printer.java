package research.analysis;

import soot.*;
import soot.jimple.*;
import java.util.*;

import research.analysis.Implies;

/**
 * Printer in Z3 SMT Solver format. (by Microsoft Research)
 */
public class Z3Printer extends AbstractJimpleValueSwitch implements Printer {
	private StringBuilder _builder;

	/**
	 * Prints set of implications into string builder in Z3 format
	 */
	public void print(StringBuilder sb, Set<Implies> defs) {
		_builder = sb;
		int i = 0, size = defs.size();
		sb.append("(assert ");
		for (Implies imp : defs) {
			if(i++ < size - 1)
				_builder.append("(and ");
			print(imp);
			if(i < size)
				_builder.append(' ');
		}
		for(; i > 1; i--)
			_builder.append(')');
		sb.append(")\n(check-sat)\n(get-model)\n");
	}

	// appends implication into string builder
	private void print(Implies imp) {
		_builder.append("(=> ");
		// print left conjuction
		int i = 0, size = imp.getLeftExpr().size();
		for (Value v : imp.getLeftExpr()) {
			if(i++ < size - 1)
				_builder.append("(and ");
			defaultCase(v);
		}
		for(; i > 1; i--)
			_builder.append(')');
		_builder.append(' ');
		defaultCase(imp.getRightExpr());
		_builder.append(')');
	}

	// **** LOGICAL EXPRS ****
	public void caseAndExpr(AndExpr v) { // &&
		_builder.append("(and ");
		appendBinopExpr(v);
	}

	public void caseOrExpr(OrExpr v) { // ||
		_builder.append("(or ");
		appendBinopExpr(v);
	}

	public void caseNegExpr(NegExpr v) { // !
		_builder.append("(not ");
		defaultCase(v.getOp());
		_builder.append(')');
	}

	// **** COMPARISON EXPRS ****
	public void caseCmpExpr(CmpExpr v) { 
		G.v().out.println("Cmp expr encountered: " + v.toString());
	}

	public void caseCmpgExpr(CmpgExpr v) { // >
		G.v().out.println("Cmpg expr encountered: " + v.toString());
	}

	public void caseGtExpr(GtExpr v) { // >
		_builder.append("(> ");
		appendBinopExpr(v);
	}

	public void caseGeExpr(GeExpr v) { // >=
		_builder.append("(>= ");
		appendBinopExpr(v);
	}

	public void caseCmplExpr(CmplExpr v) { // <
		G.v().out.println("Cmpl expr encountered: " + v.toString());
	}

	public void caseLtExpr(LtExpr v) { // <
		_builder.append("(< ");
		appendBinopExpr(v);
	}

	public void caseLeExpr(LeExpr v) { // =<
		_builder.append("(<= ");
		appendBinopExpr(v);
	}

	public void caseEqExpr(EqExpr v) { // ==
		_builder.append("(= ");
		appendBinopExpr(v);
	}

	public void caseNeExpr(NeExpr v) { // != 
		_builder.append("(not (= ");
		appendBinopExpr(v);	
		_builder.append(')');
	}

	// **** NUMBER ARITHMETICS ****
	public void caseDivExpr(DivExpr v) { // /
		_builder.append("(div ");
		appendBinopExpr(v);
	}

	public void caseMulExpr(MulExpr v) { // *
		_builder.append("(* ");
		appendBinopExpr(v);
	}

	public void caseSubExpr(SubExpr v) { // -
		_builder.append("(- ");
		appendBinopExpr(v);
	}

	public void caseAddExpr(AddExpr v) { // +
		_builder.append("(+ ");
		appendBinopExpr(v);
	}

	// **** CONSTANTS ****
	public void caseDoubleConstant(DoubleConstant v) {
		_builder.append(v.value);
	}

	public void caseFloatConstant(FloatConstant v) {
		_builder.append(v.value);
	}

	public void caseIntConstant(IntConstant v) {
		_builder.append(v.value);
	}

	public void caseLongConstant(LongConstant v) {
		_builder.append(v.value);
	}

	public void caseNullConstant(NullConstant v) {
		assert false : "NullConstant not accepted";
	}

	// **** VARIABLES ****
	public void caseLocal(Local v) {
		if(v instanceof RLocal)
			defaultCase(((RLocal)v).getReplacedBy());
		else
			_builder.append(v.getName());
	}

	// **** HELPER METHODS ****
	public void defaultCase(Object v) {
		((Value)v).apply(this);
	}

	private void appendBinopExpr(BinopExpr v) {
		defaultCase(v.getOp1());
		_builder.append(' ');
		defaultCase(v.getOp2());
		_builder.append(')');
	}
}