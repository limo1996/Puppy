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
                error()
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

  