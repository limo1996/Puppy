
public class Test {
	
	public static void main(String[] args) {	
		int i0 = 0;
		int i1 = 1;

		if (i0 < 10) {
			i0 = i0 + i1 + 8;
			i1 = i0;
		} else {
            i0 = 9;
			i1 = 5;
        }

        System.out.println(i0 + i1);
		return;
	}

    void ex1(int a, int b, int c) {
        int d;
        if(c == 0){
            d = a;
        } else {
			d = b;
			error();
        }
        System.out.println(d);
    }

    void ex2(int a){
        int d = 0;
        if(a == 0){
			d = 5;
			error();
        }
        System.out.println(d);
    }

    void ex3(int a){
        int d = 0;
        if(a == 0){
            d = 5;
        } else {
			d = 6;
			error();
        }
        System.out.println(d);
    }

    void ex4(int a){
        int d = 0;
        if(a == 0){
            d = 5;
        } else {
            d = 6;
        }
        if(a >7){
			d = 8;
			error();
        }
        System.out.println(d);
    }

    void ex5(int a){
        int d = 0;
        if(a == 0){
            return;
        } else {
            d = 6;
        }
        if(a >7){
			d = 8;
			error();
        }
        System.out.println(d);
    }

    void ex6(int a, int b){
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
			error();
        }

        System.out.println(a + b + c + d);
    }

    void ex7(int a) {
        int d = 0;
        if(a < 0)
			d = 1;
		error();
        System.out.print(d);
	}
	
	void ex8(int a, int b, int c) {
        int d = 0;
        if(!(a + b + c * (a + b) < 0) && 7 < 8) {
			d = 1;
			error();
		}
		if(6<9)
			d=2;
        System.out.print(d);
	}

	void ex9(boolean a, int b, int c) {
		int d;
		if(!a)
			d = b;
		else 
			d = c;
		if(d < b + c)
			error();
	}

	void ex10(int a, int b, int e) {
		int c, d;

		if(a == 0 || b == 0){
            d = a + b;
            if(d == 9 && a == 0){
                d = a;
			}
			if(d == e){
				error();
			}
        } else {
            
        }
	}

	void ex11(int a, int b, int c) {
        int d, e;
        if(c == 0){
			d = a;
			e = b;
        } else {
			d = b;
			e = a;
		}
		
        if(d <= e){
            error();
        }
	}
	
	void ex12(int a, int b, int c) {
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

        if(d + e<= f){
            error();
        }
	}
	
	void ex13(int a, int b, int c) {
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
            error();
		}
	}

	void ex14(int a) {
		int b = 0, c = 1;
		if(a == 0){
			b = 1;
		}
		if(b == 0){
			c = a;
		}

		if(c >= 0){
			error();
		}
	}
	
	void error() {
		int a = 1 / 0;
		System.out.print(a);
	}
}
