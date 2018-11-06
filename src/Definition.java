package research.analysis;

import soot.Value;

interface Definition {
    Definition join(Definition other);
    boolean containsCondition(Value condition);
}