package research.analysis;

import soot.Value;
import soot.Local;
import soot.jimple.internal.*;

/**
 * Since binary expressions in soot can contain only immediate values because of
 * the complex expression transformation into binary ones with tmp variables,
 * we had to find a solution to create a more complex ASTs. We accomplished that 
 * by subclassing JimpleLocal and adding replacedBy field which can contain 
 * any Value. Note that whenever you encounter RLocal in AST it guarantees that 
 * this local was replaced and field replacedBy is not null.
 */
public class RLocal extends JimpleLocal {
	private Value replacedBy; 
	private Local original;

	/**
	 * Creates new instance of RLocal.
	 * @param l	Local to be replaced.
	 * @param replacedBy Value which @param l is going to be replaced with.
	 */
	public RLocal(Local l, Value replacedBy) {
		super(l.getName(), l.getType());
		this.replacedBy = Utils.clone(replacedBy, true);
		this.original = l;
	}

	@Override
	public Object clone() {
		return new RLocal(original, replacedBy);
	}
	
	// **** GETers and SETers ****
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