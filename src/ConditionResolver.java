package research.analysis;

import soot.*;
import soot.jimple.*;
import soot.jimple.internal.JimpleLocal;

import java.util.*;

/**
 * Resolves conditions in the sense that it replaces original variables with their conditional
 * definitions until final formulas are "pure" i.e. contain only paramaters to the functions.
 * For each condition to resolve, resolver returns conjuction of implications which represent
 * orinal condition resolved to contain only pure variables.
 */
public class ConditionResolver extends AbstractBaseSwitch {
	private Map<String, Definition> _definitions;
	private NParamFinder finder;
	private LocalReplacer replacer;
	private Local tmp_local;
	private Settings _settings;

	/**
	 * Creates new instance of ConditionResolver.
	 * @param definitions at the beginning of the basic block we want to reach.
	 * @param settings app settings.
	 */
	public ConditionResolver(Map<Local, Definition> definitions, Settings settings) {
		_settings = settings;
		_definitions = new HashMap<String, Definition>();
		for (Map.Entry<Local, Definition> entry : definitions.entrySet())
			_definitions.put(entry.getKey().getName(), entry.getValue());
		finder = new NParamFinder(_definitions, this);
		replacer = new LocalReplacer(this);
		tmp_local = new JimpleLocal("something_that_nobody_guesses_reven_hjsfbjhebfhjsebfq8746q873468", BooleanType.v());
	}

	// unfortunately we don't have generic visitor and therefore we cannot return
	// from methods so we need to have field return which every method has to set
	// to its result
	private Set<Implies> result;

	/**
	 * Resolves condition to conjuction of implications that contain only parameter variables.
	 */
	public Set<Implies> resolve(Value condition) {
		debug("Resolver: Resolving " + condition.toString());
		result = null;
		defaultCase(condition);
		return result;
	}

	public void unopExpr(UnopExpr v) { // !
		defaultCase(v);
		for (Implies i : result) {
			UnopExpr nnew = (UnopExpr)v.clone();
			nnew.getOpBox().setValue(i.getRightExpr()); // negate results of children
			i.setRightExpr(nnew);
		}
	}

	public void binopExpr(BinopExpr v) { // <, >, <=, >=, !=, ==, +, -, /, *
		ValueBox vb1 = v.getOp1Box();
		ValueBox vb2 = v.getOp2Box();
		Set<Implies> left, right, res;
		defaultCase(v.getOp1());
		left = result;
		defaultCase(v.getOp2());
		right = result;
		res = new HashSet<Implies>();

		// for all combinations put left parts in the new left part of implication
		// and new right side will be 'left_right op right_right'
		for(Implies i1 : left){
			for(Implies i2 : right){
				BinopExpr bnew = (BinopExpr)v.clone();
				bnew.getOp1Box().setValue(Utils.getFreshRLocal(i1.getRightExpr()));
				bnew.getOp2Box().setValue(Utils.getFreshRLocal(i2.getRightExpr()));
				Implies inew = new Implies(i1.getLeftExpr(), bnew);
				inew.getLeftExpr().addAll(i2.getLeftExpr());
				res.add(inew);
			}
		}
		result = res;
	}

	public void caseLocal(Local v) {
		/** 
		* 1. get definition of local
		* 2. for every not param local in the definition resolve it
		* 3. Step 2 si probably good to do with another visitor maybe

		* FACTS:
		* definition of every variable is set of implications so this method has to create them
		* EXAMPLE
		* condition	definition
		* a == 0		c = a
		* a != 0		c = b
		* c > 8		e = a + d
		* c <= 8		e = b + c
		* b == 0		d = a
		* b != 0		d = b
		* e == 0		f = 2*d
		* e != 0		f = d/2

		* RESOLVE f TO PARAM:
		* Ok deep breath. 
		* RESOLVE f: e == 0 ==> 2*d && e != 0 ==> d/2
		* Now we see that these expressions contains locals that are not parameters
		* Next iteration will be with visitor separately on both operands of AND
		* Non param locals in the expression on the left hand side of implication will be replaced with their
		* definitions and corresponding conditions will be conjoined to the expression(if more then 2 then for
		* every definition/condition pair original formula will be duplicated)
		* i.e. e == 0 will become 	c > 8 && a + d == 0
		*							c <= 8 && b + c == 0
		* now we need to resolve c and d
		* c: a == 0 ==> a && a != 0 ==> b finally we are pure here
		* plug def. of c in: 	a == 0 && a > 8 && a + d == 0
		*						a != 0 && a > 8 && a + d == 0
		*						a == 0 && a <= 8 && b + a == 0
		*						a != 0 && b <= 8 && b + b == 0
		*
		* Similarly we resolve the d's definition. 
		*
		* In case of right side of implication we substitute on the right and put conditions of definition
		* to the right side. i.e.
		* Resolving d in e == 0 ==> 2*d will be:
		* def. of d: b == 0 ==> a && b != 0 ==> b
		* so resolved right hand side of the implication will be:
		* e == 0 && b == 0 ==> 2*a
		* e == 0 && b != 0 ==> 2*b
		*/

		// If v is RLocal we continue with traversal
		if(v instanceof RLocal) {
			defaultCase(((RLocal)v).getReplacedBy());
			return;
		}

		debug("Resolver: Processing local " + v.toString(), 1);
		Set<Implies> pure_defs = new HashSet<Implies>();

		// If Local is parameter than the result is (true ==> v) Node that true is in fact empty set
		// Otherwise we resolve every definition of v (set of implications)
		if(isParam(v)) {
			pure_defs.add(new Implies(new HashSet<Value>(), v));
		} else {
			LocalDefinition ld = (LocalDefinition)_definitions.get(v.getName());
			for (Map.Entry<Set<Value>, Value> entry : ld.getDefinitions().entrySet()) {
				RLocal right = new RLocal(tmp_local, entry.getValue());
				pure_defs.addAll(resolveDefinition(new Implies(entry.getKey(), right)));
			}
		}

		debug("Resolver: Local defs: " + pure_defs.toString(), 1);
		result = pure_defs;
	}

	// returns true if var is parameter
	private boolean isParam(Local var) {
		LocalDefinition ld = (LocalDefinition)_definitions.get(var.getName());
		Map.Entry<Set<Value>, Value> def = ld.getDefinitions().entrySet().iterator().next();
		return def.getKey().isEmpty() && def.getValue() instanceof ParameterRef;
	}

	// resolves left hand side of the implication
	private List<Implies> resolveDefinition(Implies def) {
		List<Implies> replaced = new ArrayList<Implies>();
		Queue<Implies> toProcess = new LinkedList<Implies>();
		toProcess.add(def);

		/**
		 * High level idea of the algorithm below:
			 * In every iteration poll new value out of the queue
			 * Find finder find first not param local in left side of impl.
			 * If all are parameters then find it in right side
			 * |	If right is pure as well then push curr implication to resulting sequence
			 * |	'-> Otherwise for every definition of found not param push its condition
			 * |		on the left side of new copy of curr implication and replace found not param
			 * |		with its current definition everywhere in the new implication
			 * '->  Otherwise for every definition of found not param push its condition
			 * 		on the left side of the new copy and replace found not param with 
			 * 		the current definition.
			 * Return resulting sequence.
		 */
		while(!toProcess.isEmpty()) {
			Implies curr = toProcess.poll();
			Local toReplace = null;
			debug("Resolver: Processing " + curr.getLeftExpr().toString() + " ==> " + curr.getRightExpr().toString(), 1);

			for(Value v : curr.getLeftExpr()) {
				toReplace = finder.findFirstNotParam(v);
				if(toReplace != null)
					break;
			}
			
			if(toReplace == null) {
				toReplace = finder.findFirstNotParam(curr.getRightExpr()); 
				if(toReplace == null)
					replaced.add(curr);
				else {
					LocalDefinition ld = (LocalDefinition)_definitions.get(toReplace.getName());
					for (Map.Entry<Set<Value>, Value> entry : ld.getDefinitions().entrySet()) {
						Implies curr_clone = new Implies(curr);
						Value rexpr = curr_clone.getRightExpr();
						replaceRight(toReplace, entry.getValue(), curr_clone);
						for(Value v : entry.getKey())
							curr_clone.getLeftExpr().add(Utils.clone(v));

						toProcess.add(curr_clone);
					}
				}
			} else {
				LocalDefinition ld = (LocalDefinition)_definitions.get(toReplace.getName());
				for (Map.Entry<Set<Value>, Value> entry : ld.getDefinitions().entrySet()) {
					Implies curr_clone = new Implies(curr);
					for (Value v : curr_clone.getLeftExpr()) {
						replacer.replace(toReplace, entry.getValue(), v);
					}
					replaceRight(toReplace, entry.getValue(), curr_clone);
					curr_clone.addLeftExpr(entry.getKey());
					toProcess.add(curr_clone);
				}
			}
		}
		return replaced;
	}

	// Replaces locals *with* in right part of implication *curr* if they equals *what*
	private void replaceRight(Local what, Value with, Implies curr) {
		Value rexpr = curr.getRightExpr();
		if(rexpr instanceof Local && ((Local)rexpr).getName().equals(what.getName()))
			curr.setRightExpr(new RLocal(what, with));
		else {
			replacer.replace(what, Utils.clone(with), rexpr);
		}
	}

	/**
	 * Return implication in form: true ==> constant
	 * @param v Constant in AST
	 */
	public void numericConstant(NumericConstant v) {
		debug("Resolver: Numeric constant " + v.toString());
		result = new HashSet<Implies>();
		result.add(new Implies(new HashSet<Value>(), v));
	}

	public void debug(String msg){
        debug(msg, 1);
    }

    public void debug(String msg, int debug){
        debug(msg, debug, true);
    }

    // debug output
    public void debug(String msg, int level, boolean newLine) {
        _settings.debug(msg, level, newLine);
    }

    protected void test(String msg) {
        test(msg, true);
    }

    // appends to output file which will be tested
    protected void test(String msg, boolean newLine) {
        _settings.test(msg, newLine);
    }
}