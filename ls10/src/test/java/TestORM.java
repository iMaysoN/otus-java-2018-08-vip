import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import orm.client.DBExecutor;
import orm.db.DBHelper;
import orm.db.UserDataSet;

import java.sql.SQLException;

public class TestORM {
    private static DBExecutor exec;

    @BeforeAll
    public static void showMustGoOn() {
        exec = new DBExecutor();
        DBHelper.generateDefaultTable(exec.getConnection());
    }

    @ParameterizedTest
    @CsvSource({"13, bugs bunny, 14", "2, spider-man, 20", "100500, batman, 50"})
    public void commonOrmTest(final int id, final String name, final int age) throws SQLException, ReflectiveOperationException {
        final UserDataSet uds = new UserDataSet(id, name, age);
        exec.save(uds);
        System.out.println("  TO DB: " + uds.toString());
        UserDataSet udl = exec.load(id, UserDataSet.class);
        System.out.println("From DB: " + udl);
        Assertions.assertEquals(uds, udl);
    }

    @AfterAll
    public static void showMustEnd() throws Exception {
        exec.close();
    }
}
