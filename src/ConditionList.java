package research.analysis;

import java.util.Set;
import java.util.HashSet;
import soot.Value;

class ConditionList implements Definition {
    private Set<Value> _currConditions;

    public ConditionList(){
        _currConditions = new HashSet<Value>();
    }

    public Definition join(Definition other) {
        if(other instanceof ConditionList) {
            ConditionList cOther = (ConditionList)other;
            _currConditions.retainAll(cOther._currConditions);
        }
        return this;
    }

    void add(Value condition) {
        _currConditions.add(condition);
    }

    public Set<Value> getConditions() {
        return _currConditions;
    }

    public boolean containsCondition(Value condition) {
        return _currConditions.contains(condition);
    }

    @Override
    public String toString() {
        return _currConditions.toString();
    }

    @Override
    public ConditionList clone() {
        ConditionList cl = new ConditionList();
        cl._currConditions = new HashSet<Value>(this._currConditions);
        return cl;
    }
}