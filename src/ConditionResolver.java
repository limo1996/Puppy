package research.analysis;

import soot.*;
import soot.jimple.*;
import soot.jimple.internal.JimpleLocal;

import java.util.*;

public class ConditionResolver extends AbstractBaseSwitch {
	private Map<String, Definition> _definitions;
	private NParamFinder finder;
	private LocalReplacer replacer;
	private Local tmp_local;
	private Settings _settings;

	public ConditionResolver(Map<Local, Definition> definitions, Settings settings) {
		_settings = settings;
		_definitions = new HashMap<String, Definition>();
		for (Map.Entry<Local, Definition> entry : definitions.entrySet())
			_definitions.put(entry.getKey().getName(), entry.getValue());
		finder = new NParamFinder(_definitions);
		replacer = new LocalReplacer();
		tmp_local = new JimpleLocal("something_that_nobody_guesses_reven_hjsfbjhebfhjsebfq8746q873468", BooleanType.v());
	}

	// unfortunately we don't have generic visitor and therefore we cannot return
	// from methods so we need to have field return which every method has to set
	// to its result
	private Set<Implies> result;

	public Set<Implies> resolve(Value condition) {
		//G.v().out.println("Resolving " + condition.toString());
		result = null;
		defaultCase(condition);
		//G.v().out.println("Resolving end " + result.toString());
		return result;
	}

	public void unopExpr(UnopExpr v) { // !
		//G.v().out.println("UnopExpr " + v.toString());
		defaultCase(v);
		for (Implies i : result) {
			UnopExpr nnew = (UnopExpr)v.clone();
			nnew.getOpBox().setValue(i.getRightExpr());
			i.setRightExpr(nnew);
		}
	}

	public void binopExpr(BinopExpr v) {
		//G.v().out.println("BinopExpr " + v.toString());
		ValueBox vb1 = v.getOp1Box(), vb2 = v.getOp2Box();
		defaultCase(v.getOp1());
		Set<Implies> left = result;
		defaultCase(v.getOp2());
		Set<Implies> right = result;
		//G.v().out.println(v.toString() + "= {left: " + left.toString() + " right: " + right.toString() + "}");
		Set<Implies> res = new HashSet<Implies>();
		for(Implies i1 : left){
			for(Implies i2 : right){
				Value bnew = Utils.getNew(v, i1.getRightExpr(), i2.getRightExpr());
				Implies inew = new Implies(i1.getLeftExpr(), bnew);
				inew.getLeftExpr().addAll(i2.getLeftExpr());
				res.add(inew);
			}
		}
		result = res;
	}

	public void caseLocal(Local v) {
		// TODO:
		// 1. get definition of local
		// 2. for every not param local in the definition resolve it
		// 3. Step 2 si probably good to do with another visitor maybe

		// FACTS:
		// definition of every variable is set of implications so this method has to create them
		// EXAMPLE
		// condition	definition
		// a == 0		c = a
		// a != 0		c = b
		// c > 8		e = a + d
		// c <= 8		e = b + c
		// b == 0		d = a
		// b != 0		d = b
		// e == 0		f = 2*d
		// e != 0		f = d/2

		// RESOLVE *f* TO PARAM:
		// Ok deep breath. 
		// RESOLVE f: e == 0 ==> 2*d && e != 0 ==> d/2
		// Now we see that these expressions contains locals that are not parameters
		// Next iteration will be with visitor separately on both operands of AND
		// Non param locals in the expression on the left hand side of implication will be replaced with their
		// definitions and corresponding conditions will be conjoined to the expression(if more then 2 then for
		// every definition/condition pair original formula will be duplicated)
		// i.e. e == 0 will become 	c > 8 && a + d == 0
		//							c <= 8 && b + c == 0
		// now we need to resolve c and d
		// c: a == 0 ==> a && a != 0 ==> b finally we are pure here
		// plug def. of c in: 	a == 0 && a > 8 && a + d == 0
		//						a != 0 && a > 8 && a + d == 0
		//						a == 0 && a <= 8 && b + c == 0
		//						a != 0 && b <= 8 && b + c == 0
		//G.v().out.println("Local " + v.toString());
		Set<Implies> pure_defs = new HashSet<Implies>();
		if(isParam(v))
			pure_defs.add(new Implies(new HashSet<Value>(), v));
		else {
			LocalDefinition ld = (LocalDefinition)_definitions.get(v.getName());
			for (Map.Entry<Set<Value>, Value> entry : ld.getDefinitions().entrySet()) {
				RLocal right = new RLocal(tmp_local, entry.getValue());
				pure_defs.addAll(resolveDefinition(new Implies(entry.getKey(), right)));
			}
		}
		//G.v().out.println("Local defs: " + pure_defs.toString());
		result = pure_defs;
	}

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
		int i = 0;
		while(!toProcess.isEmpty() && i++ < 100) {
			Implies curr = toProcess.poll();
			//G.v().out.println("Processing " + curr.getLeftExpr().toString() + " ==> " + curr.getRightExpr().toString());
			Local toReplace = null;
			for(Value v : curr.getLeftExpr()) {
				toReplace = finder.findFirstNotParam(v);
				if(toReplace != null)
					break;
			}
			//G.v().out.println("11111111");
			if(toReplace == null) {
				toReplace = finder.findFirstNotParam(curr.getRightExpr()); // <- error
				//G.v().out.println("22222222 ");
				if(toReplace == null)
					replaced.add(curr);
				else {
					//G.v().out.println("333333333");
					LocalDefinition ld = (LocalDefinition)_definitions.get(toReplace.getName());
					//G.v().out.println(ld.getDefinitions().toString());
					for (Map.Entry<Set<Value>, Value> entry : ld.getDefinitions().entrySet()) {
						//G.v().out.println("333333333.11111111");
						Implies curr_clone = new Implies(curr);
						//G.v().out.println("333333333.22222222");
						//G.v().out.println(toReplace + " " + replacer + " " + entry.getValue().toString() + " " + curr_clone.getRightExpr().toString());
						Value rexpr = curr_clone.getRightExpr();
						/*if(rexpr instanceof Local && ((Local)rexpr).getName().equals(toReplace.getName()))
							curr_clone.setRightExpr(entry.getValue());
						else 
							replacer.replace(toReplace, (Value)entry.getValue().clone(), curr_clone.getRightExpr());*/
						replaceRight(toReplace, entry.getValue(), curr_clone);
						//G.v().out.println("333333333.333333333");
						for(Value v : entry.getKey())
							curr_clone.getLeftExpr().add(Utils.clone(v));
						//G.v().out.println("333333333.44444444444");
						toProcess.add(curr_clone);
						//G.v().out.println("333333333.44444444444");
					}
					//G.v().out.println("44444444444");
				}
			} else {
				//G.v().out.println("5555555555");
				LocalDefinition ld = (LocalDefinition)_definitions.get(toReplace.getName());
				for (Map.Entry<Set<Value>, Value> entry : ld.getDefinitions().entrySet()) {
					//G.v().out.println("66666666666");
					Implies curr_clone = new Implies(curr);
					for (Value v : curr_clone.getLeftExpr()) {
						replacer.replace(toReplace, entry.getValue(), v);
					}
					replaceRight(toReplace, entry.getValue(), curr_clone);
					curr_clone.addLeftExpr(entry.getKey());
					//G.v().out.println("Replaced: " + curr_clone.toString());
					toProcess.add(curr_clone);
					//G.v().out.println("66666666666.44444444444");
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

	protected void debug(String msg){
        debug(msg, 1);
    }

    protected void debug(String msg, int debug){
        debug(msg, debug, true);
    }

    // debug output
    protected void debug(String msg, int level, boolean newLine) {
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