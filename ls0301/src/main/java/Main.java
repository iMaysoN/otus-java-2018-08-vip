import java.util.Collections;

public class Main {
    public static void main(String[] args) {

        MyTrueList<String> srclst = new MyTrueList<String>(5);
        MyTrueList<String> destlst = new MyTrueList<String>(10);

        // populate two lists
        srclst.add("Java");
        srclst.add("is");
        srclst.add("best");

        destlst.add("C++");
        destlst.add("is");
        destlst.add("older");

        // copy into dest list
        Collections.copy(destlst, srclst);

        System.out.println("Value of source list: " + srclst);
        System.out.println("Value of destination list: " + destlst);

        Collections.sort(srclst);
        System.out.println("Value of source list: " + srclst);

        Collections.sort(srclst, Collections.reverseOrder());
        System.out.println("Value of source list: " + srclst);

        Collections.addAll(destlst, "Test", "it", "faster!");
        System.out.println("Value of source list: " + destlst);
    }
}
