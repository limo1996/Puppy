package research.analysis;

import java.util.Set;
import java.util.HashSet;
import soot.Value;

/**
 * Class that contains information about depth of the current branch. 
 * It contains conditions that needs to be met in orfer to be in current branch.
 */
class ConditionList implements Definition {
    private Set<Value> _currConditions;

    public ConditionList(){
        _currConditions = new HashSet<Value>();
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
            newcl._currConditions = new HashSet<Value>(this._currConditions);
            ConditionList cOther = (ConditionList)other;
            newcl._currConditions.retainAll(cOther._currConditions);
            return newcl;
        }
        return null;
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
        return _currConditions;
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
        cl._currConditions = new HashSet<Value>(this._currConditions);
        return cl;
    }
}