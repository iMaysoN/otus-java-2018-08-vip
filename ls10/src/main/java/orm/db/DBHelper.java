package orm.db;

import orm.client.DBClient;

import java.sql.Statement;

public class DBHelper {
    public static String dbName = "orm";
    private static final String CREATE_TABLE = String.format("create table %s " +
                    "(" +
                    "id bigint(20) NOT NULL auto_increment,%n" +
                    "name varchar(255),%n" +
                    "age int(3),%n" +
                    "address varchar(255),%n" +
                    ")",
            dbName);

    public static void generateDefaultTable(DBClient client) {
        try (final Statement statement = client.createStatement()) {
            statement.executeUpdate(CREATE_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
