public class Test {	
	public static void main(String[] args) {	
		Test t = new Test();
		t.ex1(0,0,1);
		t.ex2(0);
		t.ex3(1);
		t.ex4(8);
		t.ex5(8);
		t.ex6(0,0);
		t.ex7(0);
		t.ex8(-5,7,6);
		t.ex9(1,1,0);
		t.ex10(0,0,0);
		t.ex11(-1,0,0);
		t.ex12(0,2,-1);
		t.ex13(-3,-1,-1);
		t.ex14(0);
		t.ex15(1,1,1);
		t.ex16(0,2,3);
		t.ex17(4,2,3);
		t.ex18(0,8,10);
		t.ex19(2,1,2);
		t.ex20(4,2,3);
		t.curly(0,0,1);
		t.swapping(0,0);
		t.bright(0,0,6,-2,-2,5,-2);
		t.div(0);
		t.lon(0);
		t.javali1(-1,-1,0,-1);
		t.javali2(3,2,2);
		t.deepPure(0,0,0,0,7,0,0,0);
		t.lol(3);
		t.lol2(3);
		t.lol3(4);
		t.deep_mini(-6,-6,-6,-4,-2,-2);
		t.deep(-1,-1,0,0,0,0);
		t.deepProp(5,-5,-13);
	}

    void ex1(int a, int b, int c) { // 0 0 1
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
	}

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
	
	void ex19(int a, int b, int c) {
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
	}

	void ex20(int a, int b, int c) {
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
	}

	void swapping(int a, int b) {
		int x = a, y = b;
		int tmp = x;
		x = y;
		y = tmp;
		if(y == a && x == b)
			error("swapping");
	}

	void bright(int a, int b, int c, int d, int e, int f, int g) {
		int x = 0;
		if(a * b + c - x + g == c - b * a + e) {
			x = 1;
		} else {
			x = 3;
		}

		if(x * a - b == 0 && c - d * e < f + g && d * e - g == c && c + b > f) {
			if(a == 0 || b == 0 || c == 0 || d == 0 || e == f + g || f == 3 || g == 11)
				error("bright");
		}
	}

	void div(int a) {
		int x = a, y = 2*a;
		if(a == (x + y) / 3)
			error("div");
	}

	void lon(int a) {
		int x = 8, y = 9, z = 0;
		if(a == 0)
			x++;
		if(x == y) {
			x++;
			y--;
		} else {
			x++;
			y++;
		}

		if(x - y == 2) {
			z++;
			y++;
		} else {
			z--;
		}

		if(z < 0)
			return;

		if(z > 0 && x - (a + y) == 1)
			error("lon");
	}

	void javali1(int i1, int i2, int i3, int i4) {	//1 1 0 1
		int x1 = -1, x2 = -1, x3 = -1, x4 = -1;
		if(i1 == i2) {
			x1 = 4;
			if(i2 == i3) {
				x2 = 4;
				if(i1 == i3) {
					x3 = 5;
				}
			} else {
				x2 = 3;
				if(i3 == i4) {
					x3 = 1;
				} else {
					x3 = 2;
					if(i1 == i4) {	
						x4 = 1;
					}
				}
			}
		} else {
			x1 = 4;
			if(i2 == i3){
				x2 = i3 + 1;
			}
		}

		if(x1 * x2 * x3 * x4 > 0)
			error("javali1");
	} 

	void javali2(int i1, int i2, int i3) {
		int x1 = 0, x2 = 0, x3 = 0;
		if(i1 >= i2) {
			if(i2 == i1){
				x3 = i2;
			} else {
				if(i2 == i3){
					x2 = 3;
				}
				x1 = 2;
			}
		} else {
			x3 = i1 + i1 * i2;
		}

		if(i3 == i2) {
			x3 = i3 + i2;
		}

		if(x3 + x2 == 7 && x1 == 2)
			error("javali2");
	}

	void deepPure(int a, int b, int c, int d, int e, int f, int g, int h) {
		if(a == 0 && b == c && c == 2 * d + f) {
			if(b == 0 || d == 0 || g == 0 || h == 9) {
				if(e == 7 && c * e + g == h * a * f && h == 0) {
					if(g == 0 || d == 0 || c == d || h == 9) {
						if(a % 5 == 0)
							error("deepPure");
					}
				}
			}
		}
	}

	void lol(int a) {
		int x = 0, y = 0;
		if(a % 9 == 0 && a % 5 == 0) {
			x = 2 * a;
			y = 9;
		} else {
			y = 2 * a;
			x = 6;
		}

		if(y == x)
			y = y + 5;
		
		if(y > x)
			error("lol");
	}

	void lol2(int a) {
		int x = 0, y = 0;
		if(a / 9 == 0 && a / 5 == 0) {
			x = 2 * a;
			y = 9;
		} else {
			y = 2 * a;
			x = 6;
		}

		if(y == x)
			y = y + 5;
		
		if(y > x)
			error("lol2");
	}

	void lol3(int a) {
		int x = 0, y = 0;
		if(a / 9 == 0 && a / 5 == 0) {
			x = 2 * a;
			y = 9;
		} else {
			y = 2 * a;
			x = 6;
		}
		
		if(y > x)
			error("lol3");
	}

	void deep_mini(int a, int b, int c, int d, int e, int f) {
		int x = a * b + c;
		int y = d - e;
		int z = 0;
		if(x != y){
			int tmp = x;
			x = y;
			y = tmp;
		}

		if(x == f && y == a * b + c) {
			if(x == y || f == e || b == a || c == a)
				z = 9;
			else 
				z = 8;
		}

		if(z == 9) {
			if(x != y)
				error("deep_mini");
		}
	}

	void deep(int a, int b, int c, int d, int e, int f) {
		int x = a * b + c;
		int y = d - e;
		int z = 0;
		if(x != y){
			int tmp = x;
			x = y;
			y = tmp;
		}

		if(x == f && y == a * b + c) {
			if(x == y || f == e || b == a || c == a)
				z = 9;
			else 
				z = 8;
		}

		if(x != y && f == e + a && d == a && f == 2*f)
			z = z + 1;

		if(z == 9) {
			if(x != y)
				error("deep");
		}
	}

	void deepProp(int i0, int i1, int i2) {
		int l0, l1, l2, l3, l4, l5;
		l0 = i0 + i1;
		l1 = l0 * 2;
		l2 = l0 + l1;
		l3 = i0 * 2 + i2 - l2;
		l4 = l0 + l1 + l2 + l3;
		if(l4 + l3 == l2)
			l5 = l2+l1;
		else 
			l5 = l4 * l3 - l1;
		if(l5 == 9)
			error("deepProp");
	}
		
	void error(String msg) {
		System.out.println("Error:" + msg);
	}
}

/**
 * TODO:
 * Find better strategy for picking of element to replace
 * Implies has still duplicates on the left hand side  DONE
 * If implication contains on the left expression that is clearly false just dont deal with it
 * Duplicate implications (toString comparision is not a good idea since left side can be reordered)
 * Merge implications by right hand side? INCORRECT
 * Memoization?
 * Propagate values to branches for instance if(a==0) in this branch we can assume that a = 0
 * Enhance toProcess queue in following way:
	 * create map [right side of implication] : left side
	 * if implication to push has right hand side that is in the map just push left side to left side of map entry
	 *  
 */