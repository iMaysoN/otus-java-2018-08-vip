import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import orm.data.Address;
import orm.data.Phone;
import orm.data.User;
import orm.service.DBService;
import orm.service.hibernate.DBServiceHibernateImpl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class TestOrm {
    private static DBService hibernate;

    @BeforeAll
    public static void prepare() {
        hibernate = new DBServiceHibernateImpl();
    }

    @ParameterizedTest
    @CsvSource({
            "bugs bunny, 14, Moskov Kolomenskaya, 123-321",
            "spider-man, 20, NY any-roof, 111-call-spidy",
            "batman, 50, SPB Nevskaya, 321-123"})
    public void commonOrmTest(final String name, final int age, final String address, final String phone) {
        final User user = new User(name, age, phone, address);
        long id = hibernate.save(user);
        System.out.println("  TO DB: " + user.toString());
        User udl = hibernate.read(id);
        System.out.println("From DB: " + udl);
        Assertions.assertEquals(user, udl);
    }

    private static Stream<Arguments> negativeSource() {
        return Stream.of(
                Arguments.of(null, 1, "1 - address", "1 - phone"),
                Arguments.of("Batman", -1, "2 - address", "2 - phone"),
                Arguments.of("Batman", 3, null, "3 - phone"),
                Arguments.of("Batman", 4, "4 - address", null)
        );
    }

    @ParameterizedTest
    @MethodSource("negativeSource")
    public void negativeOrmTest(final String name, final int age, final String address, final String phone) {
        final User user = new User(name, age, phone, address);
        long id = hibernate.save(user);
        System.out.println("  TO DB: " + user.toString());
        User loadedUser = hibernate.read(id);
        System.out.println("From DB: " + loadedUser);
        Assertions.assertEquals(user, loadedUser);
    }

    @Test
    public void collectionTest() {
        final User user = new User("Superman", 33);
        final Set<Phone> phones = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            phones.add(new Phone("phone_number" + i, user));
        }
        user.setPhones(phones);
        user.setAddress(new Address("Krypton"));
        long id = hibernate.save(user);
        System.out.println("  TO DB: " + user.toString());
        User userFromDb = hibernate.read(id);
        System.out.println("From DB: " + userFromDb);
        Assertions.assertEquals(user, userFromDb);
    }

    @AfterAll
    public static void dismiss() throws Exception {
        hibernate.close();
    }
}
