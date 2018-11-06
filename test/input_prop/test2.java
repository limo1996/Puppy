/*
 * The expected result of processing these methods should be new statements
 * which contains only input variables and constants on the right hand side.
 * By doing it we can see the impact of input data on the other variables.
 */
class test2 {
    public test2() {
        System.out.println("Hello from test2");
    }
    public static void main(String[] args) {}

    void propagate2(int i0, int i1, int i2) {
        int l0, l1, l2, l3, l4;
        l0 = i0 + i1;
        if (l0 > 7){
            l1 = l0 * 2;
        } else {
            l1 = l0 + 7;
        }
        if(l0 > 20) {
            l2 = l0 + l1;
            l3 = i0 * 2 + i2 - l2;
            l4 = l0 + l1 + l2 + l3;
        } else {
            l2 = l0 -l1;
            l3 = i0 + 2 * i2 - l2;
            l4 = l0 - l1 + l2 - l3;
        }
        if(l2 + l3 + l4 > l2 * l3 * l4){
            System.out.println("wanna reach");
        }
    }
}