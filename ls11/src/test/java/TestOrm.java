import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import orm.data.Address;
import orm.data.Phone;
import orm.data.User;
import orm.service.DBService;
import orm.service.hibernate.DBServiceHibernateImpl;

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
        final User uds = new User(name, age, new Phone(phone), new Address(address));
        long id = hibernate.save(uds);
        System.out.println("  TO DB: " + uds.toString());
        User udl = hibernate.read(id);
        System.out.println("From DB: " + udl);
        Assertions.assertEquals(uds, udl);
    }
}
