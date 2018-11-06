package research.analysis;

import soot.*;
import soot.jimple.*;
import soot.jimple.internal.*;

import java.util.Map;
import java.util.HashMap;
import java.lang.RuntimeException;

class Utils {
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

    public static BinopExpr negate(ConditionExpr condExpr) {
		BinopExpr ret = null;
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
}