package research.analysis;

import soot.*;
import soot.jimple.*;
import soot.jimple.internal.*;

import java.util.*;
import java.lang.RuntimeException;

/**
 * Typical Utils static class.
 */
class Utils {
    /**
     * Returns default value of the local.
     */
    public static Value getDefaultValue(JimpleLocal local) {
        if(local.getType() instanceof IntType)
            return IntConstant.v(0);
        else if(local.getType() instanceof RefLikeType)
            return NullConstant.v();

        return IntConstant.v(0);
        // throw new RuntimeException("Type not supported or default value missing! Type: " + local.getType());
        // TODO: Add following prim. types:
        // BooleanType, ByteType, CharType, DoubleType, 
        // FloatType, Integer127Type, Integer1Type, 
        // Integer32767Type, IntType, LongType, ShortType
        // with their corresponding default values.
    }

    /**
     * Negates conditional expression.
     */
    public static ConditionExpr negate(ConditionExpr condExpr) {
		ConditionExpr ret = null;
		if(condExpr != null){
			Value lhs = condExpr.getOp1();
			Value rhs = condExpr.getOp2();
			if(condExpr instanceof EqExpr){
				ret = new JNeExpr(lhs, rhs);
			} else if(condExpr instanceof NeExpr){
				ret = new JEqExpr(lhs, rhs);
			} else if(condExpr instanceof LeExpr){
				ret = new JGtExpr(lhs, rhs);
			} else if(condExpr instanceof GtExpr){
				ret = new JLeExpr(lhs, rhs);
			} else if(condExpr instanceof GeExpr){
				ret = new JLtExpr(lhs, rhs);
			} else if(condExpr instanceof LtExpr){
				ret = new JGeExpr(lhs, rhs);
			} 
		}
		return ret;
	}

	/**
	 * Converts value of every subclass of NumericConstant to double and returns it.
	 * @param constant Constant which value will be taken
	 * @return value of the constant 
	 */
	public static double getValue(NumericConstant constant) {
		double result = 0;
		if (constant instanceof FloatConstant)
			result = ((FloatConstant)constant).value;
		else if (constant instanceof DoubleConstant)
			result = ((DoubleConstant)constant).value;
		else if (constant instanceof IntConstant)
			result = ((IntConstant)constant).value;
		else {
			assert constant instanceof LongConstant;
			result = ((LongConstant)constant).value;
		}
		return result;
	}

	public static Value clone(Value v) {
		return v;
	}

	public static Value clone(Value v, boolean deep) {
		if(v instanceof Constant || !deep)
			return v;
		Cloner c = new Cloner();
		return c.clone(v);
	}

	public static RLocal createConjuction(List<Value> target) {
		return rCreate(target, 0);
	}

	private static RLocal rCreate(List<Value> target, int i) {
		if(i == target.size() - 2)
			return getFreshRLocal(new JAndExpr(getFreshRLocal(target.get(i)), getFreshRLocal(target.get(i+1))));
		return getFreshRLocal(new JAndExpr(getFreshRLocal(target.get(i)), rCreate(target, i+1)));
	}

	// **** FRESH LOCAL CREATOR ****
	public static final String BASENAME= "something_that_nobody_guesses_reven_hjsfbjhebfhjsebfq8746q873468";
	private static int counter = 0;

	public static Local getFreshLocal() {
		return new JimpleLocal(BASENAME + counter++, BooleanType.v());
	}

	public static RLocal getFreshRLocal(Value replacedBy) {
		return new RLocal(getFreshLocal(), replacedBy);
	}

	public static RLocal createOppositeBranch(List<Value> branch) {
		assert branch.size() >= 2;
		RLocal curr = getFreshRLocal(new JOrExpr(
			getFreshRLocal(negate((ConditionExpr)branch.get(0))), 
			getFreshRLocal(new JAndExpr(getFreshRLocal(branch.get(0)),
			getFreshRLocal(negate((ConditionExpr)branch.get(1)))))));
		return createOppositeBranch(branch, 2, curr);
	}

	private static RLocal createOppositeBranch(List<Value> branch, int i, RLocal curr) {
		if(i == branch.size())
			return curr;
		List<Value> l = new ArrayList<Value>(branch.subList(0, i));
		l.add(negate((ConditionExpr)branch.get(i)));
		return createOppositeBranch(branch, i+1, getFreshRLocal(new JOrExpr(curr, createConjuction(l))));
	}
}