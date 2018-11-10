import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) {
        Bob bob = new Bob(42, "Bob");

        for (Field field : bob.getClass().getDeclaredFields()) {
            System.out.println(field.getName());
            System.out.println(field.toString());
        }

        for (Field field : bob.getClass().getSuperclass().getDeclaredFields()) {
            System.out.println(field.getName());
            System.out.println(field.toString());
        }


    }

    static class Man {
        private int age;

        public Man(int age) {
            this.age = age;
        }
    }

    static class Bob extends Man {
        private String name;

        public Bob(int age, String name) {
            super(age);
            for (Field field : this.getClass().getSuperclass().getDeclaredFields()) {
                System.out.println("--> " + field.getName());
            }
            this.name = name;
        }
    }
}
