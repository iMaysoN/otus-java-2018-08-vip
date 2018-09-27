package tests;

public class FirstTest {

    private int sum;

    @MyBefore
    public void before() {
        sum = 0;
        System.out.println("Before - " + sum);
    }

    @MyTest
    public void test1() {
        sum = sum + 5;
        System.out.println("Test - " + sum);
    }

    @MyTest
    public void test2() {
        sum = sum + 7;
        System.out.println("Test - " + sum);
    }

    @MyAfter
    public void after() {
        sum = 1;
        System.out.println("After - " + sum);
    }
}
