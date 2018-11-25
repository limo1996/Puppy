class CounterExamples {

    void ex1(int a, int b, int c) {
        int d;
        if(c == 0){
            d = a;
        } else {
            d = b;
        }
        // definition of d?
        // Condition    Definition
        //  c == 0       d = a
        // !(c == 0)     d = b
        if(d <= 0){     // must be true
            error();
        }

        // 1. step condition:   d <= 0
        // 2. step condition:   c == 0 ==> a <= 0 and c != 0 ==> b <= 0 
    }

    void ex2(int a, int b, int c, int d) {
        int e;
        if(c == 0){
            d = a + b;
            if(a == b){
                d = a;
            }
        } else {
            if(c < 0){
                d = b;
            } else {
                d = 2*b;
            }
        }
        // definition of d?
        // Condition    Definition
        // c==0          d = a + b
        // c==0 ^ a==b   d = a
        // c!=0 ^ c<0    d = b
        // c!=0 ^ c>=0   d = 2*b
        if(d <= 0){      // must be true
            error();
        }

        // 1. step condition:   d <= 0
        // 2. step condition:   (c == 0 ==> a + b <= 0) and ((c == 0 and a==b) ==> a <= 0) and ((c!=0 and c<0) ==> b <= 0) and ((c!=0 and c>=0) => 2*b <= 0
    }

    void ex3(int a, int b, int c, int d) {
        int e;
        if(c == 0){
            c = a + b;
            if(c > b){
                error();
            }
        } else {
            
        }

        // 1. step condition:   c > b
        // 2. step condition:   a + b > b
        // 3. step condition:   c == 0 and a + b > b
    }
    void error() {
        throw RuntimeException("Error");
    }

    void ex4(int a, int b){
        int c, d;
        if(a <= b){
            c = 1;
            d = 2;
        } else {
            d = 1;
            c = 2;
        }

        if(c < d){
            a = c;
            c++;
        }

        System.out.println(a + b + c + d);
	}
	
	void ex5(int a, int b, int e) {
		int c, d;

		if(a == 0){
            d = a + b;
            if(d == 9){
                d = a;
			}
			if(d == e){
				// Of	Condition			Definition
				// d	a == 0 && d == 9			a
				// d	a == 0 && d != 9			a + b
				// e	true					@param_3
				// b	true					@param_2
				// a	true					@param_1

				// 1. iter	a == 0 && d == e
				// 2. iter	a == 0 && a == 0 && d == 9 ==> a == e
				// 3. iter	a == 0 && (a == 0 && d == 9 ==> a == e) && (a == 0 && d != 9 ==> a + b == e)
				error();
			}
        } else {
            
        }
	}

	void ex6(int a, int b, int e) {
		int c, d;

		if(a == 0 || b == 0){
            d = a + b;
            if(d == 9 && a == 0){
                d = a;
			}
			if(d == e){
				// Of	Condition									Definition
				// d	(a == 0 || b == 0) && (d == 9 && a == 0)	a
				// d	(a == 0 || b == 0) && (d != 9 || a != 0)	a + b
				// e	true					@param_3
				// b	true					@param_2
				// a	true					@param_1

				// 1. iter	(a == 0 || b == 0) && d == e
				// 2. iter	(a == 0 || b == 0) && (a == 0 || b == 0) && (d == 9 && a == 0) ==> a == e
				// 3. iter	(a == 0 || b == 0) && ((a == 0 || b == 0) && (d == 9 && a == 0) ==> a == e) && ((a == 0 || b == 0) && (d != 9 || a != 0) ==> a + b == e)
				error();
			}
        } else {
            
        }
	}

	void ex7(int a, int b, int c) {
        int d, e;
        if(c == 0){
			d = a;
			e = b;
        } else {
			d = b;
			e = a;
        }

        // Condition    Definition
        //  c == 0       d = a
		// !(c == 0)     d = b
		//  c == 0       e = b
		// !(c == 0)     e = a

        if(d <= e){     // must be true
            error();
        }

        // 1. step condition:   d <= e
		// 2. step condition:   c == 0 && c == 0 ==> a <= b and c != 0 && c == 0 ==> b <= b and
		//						c == 0 && c != 0 ==> a <= a and c != 0 && c != 0 ==> b <= a
	}
	
	void ex8(int a, int b, int c) {
        int d, e, f;
        if(c == 0){
			d = a;
			e = b;
        } else {
			d = b;
			e = a;
		}
		
		if(c + b == 0){
			f = a;
		} else {
			f = b;
		}

        // Condition    Definition
        //  c == 0       d = a
		// !(c == 0)     d = b
		//  c == 0       e = b
		// !(c == 0)     e = a
		// c + b == 0	 f = a
		// c + b != 0	 f = b

        if(d + e<= f){     // must be true
            error();
        }

        // 1. step condition:   d + e <= f
		// 2. step condition:   c == 0 && c == 0 ==> a + b <= f
		//						c != 0 && c == 0 ==> a + a <= f
		//						c == 0 && c != 0 ==> b + b <= f
		//						c != 0 && c != 0 ==> b + a <= f
		// 3. step condition:	c + b == 0 && c == 0 && c == 0 ==> a + b <= a
		//						c + b != 0 && c == 0 && c == 0 ==> a + b <= b
		//						c + b == 0 && c != 0 && c == 0 ==> a + a <= a
		//						c + b != 0 && c != 0 && c == 0 ==> a + a <= b
		//						c + b == 0 && c == 0 && c != 0 ==> b + b <= a
		//						c + b != 0 && c == 0 && c != 0 ==> b + b <= b
		//						c + b == 0 && c != 0 && c != 0 ==> b + a <= a
		//						c + b != 0 && c != 0 && c != 0 ==> b + a <= b

		// get definitions for d
		// get definitions for e
		// 


		// Lemma: Every AbstractIntBinopExpr can be converted to list of implications which have con
	}
	
	void ex9(int a, int b, int c) {
        int d, e, f;
        if(c == 0){
			d = a;
			e = b;
        } else {
			d = b;
			e = a;
		}
		
		if(c + b == 0){
			f = d;
		} else {
			f = e;
		}

		int x = f + e;
        // Condition    Definition
        //  c == 0       d = a
		// !(c == 0)     d = b
		//  c == 0       e = b
		// !(c == 0)     e = a
		// c + b == 0	 f = d
		// c + b != 0	 f = e
		// true			 x = f + e
        if(x <= d) {     // must be true
            error();
		}
		// 1. step	x <= d
		// 2. step	x = f + e
		// 3. step	f = [(c + b == 0 ==> d), (c + b != 0 ==> e)]
		// 4. step	d = [(c == 0 ==> a), (c != 0 ==> b)]
		// 5. step	e = [(c == 0 ==> b), (c != 0 ==> a)]
		// 6. step merge definitions
		//			f = [(c + b == 0 && c == 0 ==> a), (c + b == 0 && c != 0 ==> b), 
		//				 (c + b != 0 && c == 0 ==> b), (c + b != 0 && c != 0 ==> a)]
		// 7. step	e = [(c == 0 ==> b), (c != 0 ==> a)]
		// 8. step	Merging two lists of definitions (Implications) for each pair AND left side and OP right side
		// 			x = [(c + b == 0 && c == 0 && c == 0 ==> a + b), 
		//				(c + b == 0 && c != 0 && c == 0 ==> b + b), 
		//				(c + b != 0 && c == 0 && c == 0 ==> b + b), 
		//				(c + b != 0 && c != 0 && c == 0 ==> a + b),
		// 				(c + b == 0 && c == 0 && c != 0 ==> a + a), 
		//				(c + b == 0 && c != 0 && c != 0 ==> b + a), 
		//				(c + b != 0 && c == 0 && c != 0 ==> b + a), 
		//				(c + b != 0 && c != 0 && c != 0 ==> a + a)]
		// 9. step Merge with d
		//				[(c + b == 0 && c == 0 && c == 0 && c == 0 ==> a + b <= b),
		//				(c + b == 0 && c != 0 && c == 0 && c == 0 ==> b + b <= b), 
		//				(c + b != 0 && c == 0 && c == 0 && c == 0 ==> b + b <= b), 
		//				(c + b != 0 && c != 0 && c == 0 && c == 0 ==> a + b <= b), 
		//				(c + b == 0 && c == 0 && c != 0 && c == 0 ==> a + a <= b), 
		//				(c + b == 0 && c != 0 && c != 0 && c == 0 ==> b + a <= b), 
		//				(c + b != 0 && c == 0 && c != 0 && c == 0 ==> b + a <= b), 
		//				(c + b != 0 && c != 0 && c != 0 && c == 0 ==> a + a <= b),
		//				(c + b == 0 && c == 0 && c == 0 && c != 0 ==> a + b <= a), 
		//				(c + b == 0 && c != 0 && c == 0 && c != 0 ==> b + b <= a), 
		//				(c + b != 0 && c == 0 && c == 0 && c != 0 ==> b + b <= a), 
		//				(c + b != 0 && c != 0 && c == 0 && c != 0 ==> a + b <= a), 
		//				(c + b == 0 && c == 0 && c != 0 && c != 0 ==> a + a <= a), 
		//				(c + b == 0 && c != 0 && c != 0 && c != 0 ==> b + a <= a), 
		//				(c + b != 0 && c == 0 && c != 0 && c != 0 ==> b + a <= a), 
		//				(c + b != 0 && c != 0 && c != 0 && c != 0 ==> a + a <= a)] 
	}

	void ex10(int a) {
		int b = 0, c = 1;
		if(a == 0){
			b = 1;
		}
		if(b == 0){
			c = a;
		}
		// Condition    Definition
		// a == 0			b = 1
		// a != 0			b = 0
		// b == 0			c = a
		// b != 0			c = 1
		if(c >= 0){
			error();
		}

		// 1. step	c >= 0
		// 2. step	c = [(b == 0 ==> a), (b != 0 ==> 1)]
		// 3. step	b = [(a == 0 ==> 1), (a != 0 ==> 0)]
		// 4. step	c = [(a == 0 ==> 1 == 0 ==> a ), gets simplified to (a == 0 && 1 == 0 ==> a)
		//				 (a != 0 && 0 == 0 ==> a), (a == 0 && 1 != 0 ==> 1), (a != 0 && 0 != 0 ==> 1)]
		// 5. step	[(a == 0 && 1 == 0 ==> a) && (a != 0 && 0 == 0 ==> a), (a == 0 && 1 != 0 ==> 1), (a != 0 && 0 != 0 ==> 1)]
	}
}


/*
 * Suppose we have just local variables + no loops + single function where input is considered to be parameters
 * Then each variable can have at some point 0 to many definitions. 
 * However we know which conditions guard these definitions 
 * (Use ForwardBranchedFlowAnalysis to remember conditions)
 * In order to compute correct input we start from block we wanna reach.
 * If we want our block to be executed we need one of his predecessors to be executed (Recursively).
 * What with function calls?? Not a problem unless they operate on fields...
 */


 /*
  * Functions needed:
        * GetDefinition(BasicBlock, int, Var) -> returns definition of var in basic block on i-th expression
                                              -> if Var is input returns either the same var or expr if it was overwrited
                                              -> returns purified def. i.e. depends only on the input
  */