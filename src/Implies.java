package research.analysis;

import soot.*;
import soot.jimple.*;
import java.util.*;

/**
 * Data class for standard mathematical implication.
 */
public class Implies {
	private Set<Value> left;
	private Set<String> left_s;
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
		this.left_s = new HashSet<String>();
		for(Value v : leftExpr) {
			tryAddLeft(v);
		}
		this.right = Utils.clone(rightExpr, true);
	}

	// **** GETTERS and SETTERS
	public void addLeftExpr(Set<Value> toAdd) {
		for(Value v : toAdd) {
			tryAddLeft(v);
		}
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

	private void tryAddLeft(Value l) {
		if(!left_s.contains(l.toString())){
			left.add(Utils.clone(l, true));
		}
		left_s.add(l.toString());
	}
}