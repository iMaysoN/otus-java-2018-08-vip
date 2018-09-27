package tests2.innertest2;

import tests.MyAfter;
import tests.MyBefore;
import tests.MyTest;

public class FailedTest {

    private int sum;

    @MyBefore
    public void before() {
        sum = 10;
        System.out.println("Before - " + sum);
    }

    @MyTest
    public void test1() {
        sum = sum + 15;
        System.out.println("Test - " + sum);
    }

    @MyTest
    public void test2() {
        sum = sum + 17;
        System.out.println("Test - " + sum);
    }

    @MyTest
    public void test3() {
        throw new RuntimeException();
    }

    @MyAfter
    public void after() {
        sum = 11;
        System.out.println("After - " + sum);
    }
}
