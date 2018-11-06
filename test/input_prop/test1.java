/*
 * The expected result of processing these methods should be new statements
 * which contains only input variables and constants on the right hand side.
 * By doing it we can see the impact of input data on the other variables.
 */
class test1 {

    public static void main(String[] args) {
        int l0, l1, l2, l3, l4, i0, i1, i2;
        i0 = 3;
        i1 = 4;
        i2 = 5;
        l0 = i0 + i1;
        l1 = l0 * 2;
        l2 = l0 + l1;
        l3 = i0 * 2 + i2 - l2;
        l4 = l0 + l1 + l2 + l3;
    }

    public void propagate1(int i0, int i1, int i2) {
        int l0, l1, l2, l3, l4;
        l0 = i0 + i1;
        l1 = l0 * 2;
        l2 = l0 + l1;
        l3 = i0 * 2 + i2 - l2;
        l4 = l0 + l1 + l2 + l3;
    }
}