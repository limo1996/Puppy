
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
        }
        System.out.println(d);
    }

    void ex2(int a){
        int d = 0;
        if(a == 0){
            d = 5;
        }
        System.out.println(d);
    }

    void ex3(int a){
        int d = 0;
        if(a == 0){
            d = 5;
        } else {
            d = 6;
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
        }

        System.out.println(a + b + c + d);
    }

    void ex7(int a) {
        int d = 0;
        if(a < 0)
            d = 1;
        System.out.print(d);
    }
}
