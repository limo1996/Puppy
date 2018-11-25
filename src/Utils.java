package research.analysis;

import soot.*;
import soot.jimple.*;
import soot.jimple.internal.*;

import java.util.Map;

import java.util.HashMap;
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
	
	public static Value getNew(Value v, Value left, Value right) {
		//G.v().out.println("Getting new for " + v.toString() + " left: " + left.toString() + " right: " + right.toString());
		if(v instanceof BinopExpr) {
			if (v instanceof CmpExpr) { return new JCmpExpr(left, right); }
			if (v instanceof CmpgExpr) { return new JCmpgExpr(left, right); }
			if (v instanceof CmplExpr) { return new JCmplExpr(left, right); }
			if (v instanceof GtExpr) { return new JGtExpr(left, right); }
			if (v instanceof GeExpr) { return new JGeExpr(left, right); }
			if (v instanceof LtExpr) { return new JLtExpr(left, right); }
			if (v instanceof LeExpr) { return new JLeExpr(left, right); }
			if (v instanceof EqExpr) { return new JEqExpr(left, right); }
			if (v instanceof NeExpr) { return new JNeExpr(left, right); }
			if (v instanceof DivExpr) { return new JDivExpr(left, right); }
			if (v instanceof MulExpr) { return new JMulExpr(left, right); }
			if (v instanceof SubExpr) { return new JSubExpr(left, right); }
			if (v instanceof AddExpr) { return new JAddExpr(left, right); }
		} else if(v instanceof UnopExpr) {
			if(v instanceof NegExpr) { return new JNegExpr(left); }
		}
		G.v().out.println("Your such a fucking whore I love it..");
		return null;
	}

	public static Value clone(Value v) {
		return v;// instanceof Constant ? v : (Value)v.clone();
	}
}