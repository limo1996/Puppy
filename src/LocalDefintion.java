package research.analysis;

import java.util.*;

import soot.Local;
import soot.Value;
import soot.G;

/**
 * Definition of local variable within a function.
 */
class LocalDefinition implements Definition {
    private Map<Set<Value>, Value> condToDefinition;
    private Local target;

    /**
     * Creates new instance of LocalDefinition with at least one conditional definition.
     * Best usage of this constructor is with current ConditionalList and definition found.
     */
    public LocalDefinition(Local definitionOf, Definition conds, Value def) {
        this.target = definitionOf;
        if(conds instanceof LocalDefinition) {
            condToDefinition = new HashMap<Set<Value>, Value>(((LocalDefinition)conds).condToDefinition);
            G.v().out.println("Do you want to do it?");
        } else if (conds instanceof ConditionList) {
            condToDefinition = new HashMap<Set<Value>, Value>();
            condToDefinition.put(((ConditionList)conds).getConditions(), def);
        }
    }

    /**
     * Creates new instance of LocalDefinition with at least one conditional definition.
     */
    public LocalDefinition(Local definitionOf, Value cond, Value def) {
        this(definitionOf);
        Set<Value> tmp = new HashSet<Value>();
        tmp.add(cond);
        condToDefinition.put(tmp, def);
    }

    /**
     * Creates new instance of empty LocalDefinition.
     */
    public LocalDefinition(Local definitionOf) {
        this.target = definitionOf;
        condToDefinition = new HashMap<Set<Value>, Value>();
    }

    /**
     * Joins two local definitions into current one and returns current one.
     */
    public Definition join(Definition other){
        if(other instanceof LocalDefinition) {
            LocalDefinition lOther = (LocalDefinition)other;
            for (Map.Entry<Set<Value>, Value> entry : lOther.condToDefinition.entrySet()) {
                Set<Value> key = entry.getKey();
                Value value = entry.getValue();
                if(this.condToDefinition.containsKey(key))
                    assert (condToDefinition.get(key) == value) : "Merging two Local Definitions with same conditions but different values!";
                this.condToDefinition.put(key, value);    // What if same condition? Can it even happen? -> assert ;)
            }
        }
        return this;
    }

    /**
     * Adds condition to each conditional definition.
     */
    public void appendCondition(Value condition) {
        for(Map.Entry<Set<Value>, Value> entry : condToDefinition.entrySet()) {
            entry.getKey().add(condition);
        }
    }

    /**
     * Returns map of conditional defintions.
     */
    public Map<Set<Value>, Value> getDefinitions() {
        return this.condToDefinition;
    }

    /**
     * Adds new definition.
     */
    public void addDefinition(Set<Value> conditions, Value value) {
        this.condToDefinition.put(conditions, value);
    }

    /**
     * Returns true if condition is present in any conditional definition.
     */
    public boolean containsCondition(Value condition) {
        for(Set<Value> key : this.condToDefinition.keySet()) {
            if(key.contains(condition))
                return true;
        }
        return false;
    }

    /**
     * Returns string representaion of the object.
     */
    @Override
    public String toString(){
        return condToDefinition.toString();
    }
}