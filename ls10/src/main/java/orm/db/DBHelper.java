package orm.db;

import orm.client.DBClient;

import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {
    private static final String CREATE_TABLE = "create table orm (id bigint(20) NOT NULL auto_increment,\n" +
            "name varchar(255),\n" +
            "age int(3));";

    public static void generateDefaultTable(DBClient client) {
        try (final Statement statement = client.createStatement()){
            statement.executeUpdate(CREATE_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
