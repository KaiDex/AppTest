public class SberTestMain {

    abstract class A {

    }

    public static void main(String[] args) {
        SberTestMain x = new SberTestMain();
        x.m(1, 3L, 1.0f);
        Object o = new Object();

    }

    private void m(Object... args) {
        throw new NullPointerException();
    }

}
