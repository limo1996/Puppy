package research.analysis;

import soot.Value;

/**
 * Interface which every definition has to implement.
 */
interface Definition {
    Definition join(Definition other);
    boolean containsCondition(Value condition);
}