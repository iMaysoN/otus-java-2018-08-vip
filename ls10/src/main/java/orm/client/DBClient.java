package orm.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBClient {
    private final Connection connection;

    public DBClient() {
        try {
            DriverManager.registerDriver(new org.h2.Driver());
            final String url = "jdbc:h2:mem:";
            connection = DriverManager.getConnection(url);
        } catch (final SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void close() throws Exception {
        connection.close();
        System.out.println("Connection closed. Bye!");
    }

    public PreparedStatement prepareStatement(final String str) throws SQLException {
        return connection.prepareStatement(str);
    }

    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }
}
