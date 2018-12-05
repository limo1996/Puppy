package research.analysis;

import soot.*;
import soot.jimple.*;
import java.util.*;

/**
 * Data class for standard mathematical implication.
 */
public class Implies {
	private Set<Value> left;
	private Value right;

	/**
	 * Creates new implication from the another.
	 * @param other
	 */
	public Implies(Implies other) {
		this(other.left, other.right);
	}

	/**
	 * Creates new implication from left and right expressions.
	 * @param leftExpr
	 * @param rightExpr
	 */
	public Implies(Set<Value> leftExpr, Value rightExpr) {
		this.left = new HashSet<Value>();
		for(Value v : leftExpr)
			this.left.add(Utils.clone(v, true));
		this.right = Utils.clone(rightExpr, true);
	}

	// **** GETTERS and SETTERS
	public void setLeftExpr(Set<Value> leftExpr) {
		assert leftExpr != null;
		this.left = leftExpr;
	}

	public void addLeftExpr(Set<Value> toAdd) {
		for(Value v : toAdd)
			this.left.add(Utils.clone(v, true));
	}

	public void setRightExpr(Value rightExpr) {
		assert rightExpr != null;
		this.right = Utils.clone(rightExpr, true);
	}

	public Set<Value> getLeftExpr() {
		return this.left;
	}

	public Value getRightExpr() {
		return this.right;
	}

	@Override
	public String toString() {
		return getLeftExpr().toString() + " ==> " + getRightExpr().toString();
	}

	public Implies deepCopy() {
		Set<Value> newleft = new HashSet<Value>();
		for(Value v : left)
			newleft.add(Utils.clone(v));
		Value newright = Utils.clone(right);
		return new Implies(newleft, newright);
	}
}