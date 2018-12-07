public class Test {	
	public static void main(String[] args) {	
		Test t = new Test();
		t.ex18(0,8,10);
	}

    /*void ex1(int a, int b, int c) { // 0 0 1
        int d;
        if(c == 0){
            d = a;
        } else {
			d = b;
			error("ex1");
        }
        System.out.println(d);
    }

    void ex2(int a){ // 0
        int d = 0;
        if(a == 0){
			d = 5;
			error("ex2");
        }
        System.out.println(d);
    }

    void ex3(int a){ // 1
        int d = 0;
        if(a == 0){
            d = 5;
        } else {
			d = 6;
			error("ex3");
        }
        System.out.println(d);
    }

    void ex4(int a){ // 8
        int d = 0;
        if(a == 0){
            d = 5;
        } else {
            d = 6;
        }
        if(a >7){
			d = 8;
			error("ex4");
        }
        System.out.println(d);
    }

    void ex5(int a){ // 8
        int d = 0;
        if(a == 0){
            return;
        } else {
            d = 6;
        }
        if(a >7){
			d = 8;
			error("ex5");
        }
        System.out.println(d);
    }

    void ex6(int a, int b){ // 0 0
        int c, d;
        if(a <= b){
            if(a==b){
                c = 1;
                d = 2;
            } else {
                c = 3;
                d = 4;
            }
        } else {
            d = 1;
            c = 2;
        }

        if(c < d){
            a = c;
			c++;
			error("ex6");
        }

        System.out.println(a + b + c + d);
    }

    void ex7(int a) { // whatever
        int d = 0;
        if(a < 0)
			d = 1;
		error("ex7");
        System.out.println(d);
	}
	
	void ex8(int a, int b, int c) { // -5 7 6
        int d = 0;
        if(!(a + b + c * (a + b) < 0) && 7 < 8) {
			d = 1;
			error("ex8");
		}
		if(6<9)
			d=2;
        System.out.println(d);
	}

	void ex9(int a, int b, int c) { // 1 1 0
		int d;
		if(a == 0)
			d = b;
		else 
			d = c;
		if(d < b + c)
			error("ex9");
	}

	void ex10(int a, int b, int e) { // 10 0 10
		int c, d;

		if(a == 0 || b == 0){
            d = a + b;
            if(d == 9 && a == 0){
                d = a;
			}
			if(d == e){
				error("ex10");
			}
        } else {
            
        }
	}

	void ex11(int a, int b, int c) { // 0 1 0
        int d, e;
        if(c == 0){
			d = a;
			e = b;
        } else {
			d = b;
			e = a;
		}
		
        if(d <= e){
            error("ex11");
        }
	}
	
	void ex12(int a, int b, int c) { // 1 0 0
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

        if(d + e<= f){ // a + b <= a
            error("ex12");
        }
	}
	
	void ex13(int a, int b, int c) { // -3 -1 1
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
        if(x <= d) {
            error("ex13");
		}
	}

	void ex14(int a) { // 0
		int b = 0, c = 1; // z0 = 0, i1 = 1
		if(a == 0){
			b = 1; //z0_1 = 1
		}
		// z0_2 = {i0 == 0 ==> z0_1, i0 != 0 ==> z0}
		if(b == 0){ // z0_2 == 0
			c = a; // i1_1 = i0
		}
		// i1_2 = {z0_2 != 0 ==> i1, z0_2 == 0 ==> i1_1}
		if(c >= 0){ // i1_2
			error("ex14");
		}
	}

	void ex15(int a, int b, int c) { // 1 1 1
		int x = 1, y = 1, z = 1;
		if(a == x || b == c || c == a || b == z) {
			z = 1;
			if(x == b && b == c && c == a){
				y = 2;
			} else {
				y = 4;
			}
		}
		if (x == 2)
			x = 1;

		if(x + y + z == 4)
			error("ex15");
	}

	void ex16(int a, int b, int c) {
		int x = 1, y = 2, z = 3, w = 4;
		if(x == b)
			z = 2;
		else {
			if(b == 2 || c == 2){
				x = 4;
				y = 3;
			} else {
				z = 3;
			}
			w = 1;
		}

		if(c == y && y > 2){
			w = 8;
		} else {
			w = 3;
		}

		if(x + y + z + w > 10)
			error("ex16");
	}
	

	void ex17(int a, int b, int c) {
		int x = 1, y = 2, z = 3, w = 4;
		if(a == c)
			x = 2;
		else
			w = 3;

		if(x == b)
			z = 2;
		else {
			if(b == 2 || c == 2){
				x = 4;
				y = 3;
			} else {
				z = 3;
			}
			w = 1;
		}

		if(c == y && y > 2) {
			w = 8;
		} else {
			w = 3;
		}

		if(w + x + y + z> 10)
			error("ex17");
	}*/

	void ex18(int a, int b, int c) {
		int x = 0;
		if(a == 0) {
			x = 2;
			if(b == 3) {
				x += 5;
				if(c == 9) {
					x *= 5;
				} else {
					x -= 4;
				}
			} else {
				x *= 3;
				if(b == 8) {
					x *= 7;
				} else {
					x = 38;
				}
			}
		} else {
			x = 1;
		}

		if(x > 40)
			error("ex18");
	}
	
	/*void ex19(int a, int b, int c) {
		int x = 1, y = 2, z = 3, w = 4;
		if(a == c)
			x = 2;
		else
			w = 3;
		if(x == b)
			z = 2;
		else {
			if(b == 2 || c == 2){
				x = 4;
				y = 3;
			} else {
				z = 3;
			}
			w = 1;
		}

		if(c == y && y > 2){
			w = 8;
		} else {
			w = 3;
		}

		if(x + y + z + w > 10)
			error("ex19");
	} */

	/*void ex20(int a, int b, int c) {
		int x = 1, y = 2, z = 3, w = 4;
		if(a == c)
			x = 2;
		else
			w = 3;
		if(x == b)
			z = 2;
		else {
			if(b == 2 || c == 2){
				x = 4;
				y = 3;
			} else {
				z = 3;
			}
		}

		if(c == y && y > 2){
			w = 8;
		} else {
			w = 3;
		}

		if(w + x + y + z > 10)
			error("ex20");
	}

	void curly(int a, int b, int c) { // 0 0 1
		int x = 0, y = 0, z = 0; 
		if (a != 0) {
			x = -2; 
		}
		if (b < 5) {
			if (a == 0 && c != 0) {
				y = 1; 
			}
			z = 2; 
		}
		if(x+y+z==3)
			error("curly");
	}*/
		
	
	void error(String msg) {
		System.out.println("Error:" + msg);
	}
}

/**
 * TODO:
 * Find better strategy for picking of element to replace
 */