package research.analysis;

import soot.Value;
import soot.Local;
import soot.jimple.internal.*;

public class RLocal extends JimpleLocal {
	private Value replacedBy; 
	private Local original;

	public RLocal(Local l, Value replacedBy) {
		super(l.getName(), l.getType());
		this.replacedBy = Utils.clone(replacedBy);
		this.original = l;
	}

	@Override
	public Object clone() {
		return new RLocal(original, Utils.clone(replacedBy));
	}
	
	public Value getReplacedBy() {
		return replacedBy;
	}

	public void setReplacedBy(Value v) {
		replacedBy = v;
	}

	@Override
	public String toString() {
		return replacedBy.toString();
	}
}