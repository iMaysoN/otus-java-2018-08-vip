public class Main {
    public static void main(String[] args) {
        MyArrayList<Integer> myArrayList = new MyArrayList<>();

        myArrayList.remove("Test");
        myArrayList.indexOf("Test");
        myArrayList.contains("Test");
        System.out.println(myArrayList);
        System.out.println("Is empty? - " + myArrayList.isEmpty());
        myArrayList.add(1);
        System.out.println(myArrayList);
        System.out.println("Is empty? - " + myArrayList.isEmpty());
        myArrayList.add(3);
        System.out.println(myArrayList);
        myArrayList.add(2);
        System.out.println(myArrayList);
        System.out.println("Size - " + myArrayList.size());
        System.out.println("Contains 3 - " + myArrayList.contains(3));
        System.out.println("Index of 3 - " + myArrayList.indexOf(3));
        myArrayList.remove(3);

        System.out.println(myArrayList);
        System.out.println("Size - " + myArrayList.size());
        System.out.println("Contains 1 - " + myArrayList.contains(1));
        System.out.println("Index of 1 - " + myArrayList.indexOf(1));

        System.out.println(myArrayList.remove("Test"));
        System.out.println(myArrayList.indexOf("Test"));
        System.out.println(myArrayList.contains("Test"));
    }
}
