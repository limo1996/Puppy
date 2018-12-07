package research.analysis;

import java.util.*;

import soot.*;
import soot.jimple.ConditionExpr;
import soot.jimple.internal.*;

/**
 * Class that contains information about depth of the current branch. 
 * It contains conditions that needs to be met in orfer to be in current branch.
 */
class ConditionList implements Definition {
	private List<Value> _currConditions;

	public ConditionList(){
		_currConditions = new ArrayList<Value>();
	}

	/**
	 * Joins current Conditional List with the other and returns new instance.
	 * Should be called when joining of two branches occurs. Internally does intersection of two sets
	 * so joined branch contains conditions from both incoming branches.
	 * Returns null if other is not of type ConditionalList.
	 */
	public Definition join(Definition other) {
		if(other instanceof ConditionList) {
			ConditionList newcl = new ConditionList();
			newcl._currConditions = new ArrayList<Value>(this._currConditions);
			ConditionList cOther = (ConditionList)other;
			newcl._currConditions.retainAll(cOther._currConditions);
			
			// if there one condition filtered out its merge of 2 previously forked branches
			if(_currConditions.size() == cOther.getConditions().size() 
				/*&& _currConditions.size() - 1 == newcl.getConditions().size()*/)
				return newcl;
				
			// else its merge of OR or merge after AND
			List<Value> left = minus(this, newcl);
			List<Value> right = minus(cOther, newcl);
			G.v().out.println(left.toString() + " " + right.toString());
			Value leftE, rightE;
			if(left.size() == 1 && right.size() > 1) {
				if(!longerAllConditionExpr(right) || left.get(0).toString().equals(Utils.createOppositeBranch(right).toString()))
					return newcl;
				leftE = Utils.getFreshRLocal(left.get(0));
				rightE = Utils.createConjuction(right);
			} else if(right.size() == 1 && left.size() > 1) {
				if(!longerAllConditionExpr(left) || right.get(0).toString().equals(Utils.createOppositeBranch(left).toString()))
					return newcl;
				leftE = Utils.getFreshRLocal(right.get(0));
				rightE = Utils.createConjuction(left);
			} else 
				return newcl;
			newcl.add(new JOrExpr(leftE, rightE));
			return newcl;
		}
		return null;
	}
	
	// Set A - Set B
	private List<Value> minus(ConditionList A, ConditionList B) {
		List<Value> missing = new ArrayList<Value>();
		for(Value v : A._currConditions)
			if(!B.getConditions().contains(v))
				missing.add(v);
		return missing;
	}

	// returns true if all Values are condition expr
	private boolean longerAllConditionExpr(List<Value> toCheck) {
		for(Value v : toCheck) {
			Value v2 = v;
			if(v instanceof RLocal)
				v2 = ((RLocal)v).getReplacedBy();
			if(!(v2 instanceof ConditionExpr)) {
				return false;
			}
		}
		return true;
	}

    /**
     * Adds new condition. This function should be called when conditional statement(if, loop)
     * is encountered. Don't forget to add negated condition to the other branch.
     */
    public void add(Value condition) {
        _currConditions.add(condition);
    }

    /**
     * Returns set of current conditions.
     */
    public Set<Value> getConditions() {
        return new HashSet<Value>(_currConditions);
    }

    /**
     * Returns true if we had to satisfy @param condition in order to reach current state.
     */
    public boolean containsCondition(Value condition) {
        return _currConditions.contains(condition);
    }

    /**
     * Returns string representation of the object.
     */
    @Override
    public String toString() {
        return _currConditions.toString();
    }

    /**
     * Creates deep copy of the current conditions list but not of the conditions itself.
     */
    @Override
    public ConditionList clone() {
        ConditionList cl = new ConditionList();
        cl._currConditions = new ArrayList<Value>(this._currConditions);
        return cl;
    }
}