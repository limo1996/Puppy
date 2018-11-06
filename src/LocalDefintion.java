package research.analysis;

import java.util.*;

import soot.Local;
import soot.Value;
import soot.G;

class LocalDefinition implements Definition {
    private Map<Set<Value>, Value> condToDefinition;
    private Local target;

    LocalDefinition(Local definitionOf, Definition conds, Value def) {
        this.target = definitionOf;
        if(conds instanceof LocalDefinition) {
            condToDefinition = new HashMap<Set<Value>, Value>(((LocalDefinition)conds).condToDefinition);
            G.v().out.println("Do you want to do it?");
        } else if (conds instanceof ConditionList) {
            condToDefinition = new HashMap<Set<Value>, Value>();
            condToDefinition.put(((ConditionList)conds).getConditions(), def);
        }
    }

    LocalDefinition(Local definitionOf, Value cond, Value def) {
        this.target = definitionOf;
        condToDefinition = new HashMap<Set<Value>, Value>();
        Set<Value> tmp = new HashSet<Value>();
        tmp.add(cond);
        condToDefinition.put(tmp, def);
    }

    public Definition join(Definition other){
        if(other instanceof LocalDefinition) {
            LocalDefinition lOther = (LocalDefinition)other;
            for (Map.Entry<Set<Value>, Value> entry : lOther.condToDefinition.entrySet()) {
                this.condToDefinition.put(entry.getKey(), entry.getValue());    // What if same condition?
            }
        }
        return this;
    }

    public void appendCondition(Value condition) {
        for(Map.Entry<Set<Value>, Value> entry : condToDefinition.entrySet()) {
            entry.getKey().add(condition);
        }
    }

    public Map<Set<Value>, Value> getDefinitions() {
        return this.condToDefinition;
    }

    public boolean containsCondition(Value condition) {
        for(Set<Value> key : this.condToDefinition.keySet()) {
            if(key.contains(condition))
                return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return condToDefinition.toString();
    }
}