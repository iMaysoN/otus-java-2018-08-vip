package orm.client;

import orm.db.DataSet;
import orm.db.UserDataSet;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DBExecutor implements AutoCloseable {
    private DBClient dbClient;

    public DBExecutor() {
        this.dbClient = new DBClient();
    }

    public <DS extends DataSet> void save(DS dataSet) throws SQLException {
        if (dataSet == null) {
            throw new RuntimeException("Data set can't be null");
        }
        String insertStatement = "insert into orm (id, name, age) values (?, ?, ?)";
        try (final PreparedStatement preparedStatement = dbClient.prepareStatement(insertStatement)) {
            preparedStatement.setLong(1, dataSet.getId());
            preparedStatement.setString(2, ((UserDataSet) dataSet).getName());
            preparedStatement.setInt(3, ((UserDataSet) dataSet).getAge());
            preparedStatement.executeUpdate();
        }

    }

    public <DS extends DataSet> DS load(final long id, final Class<DS> clazz) throws SQLException, ReflectiveOperationException {
        if (clazz == null) {
            throw new RuntimeException("Class can't be null");
        } else {
            String selectStatement = "select * from orm where id=?";
            try (final PreparedStatement preparedStatement = dbClient.prepareStatement(selectStatement)) {
                final DS dataSet = clazz.getConstructor().newInstance();
                preparedStatement.setLong(1, id);
                preparedStatement.executeQuery();
                final ResultSet resultSet = preparedStatement.getResultSet();
                final List<Field> fields = new ArrayList<>(List.of(dataSet.getClass().getDeclaredFields()));
                if (resultSet.next()) {
                    if (dataSet.getClass().getSuperclass() != null) {
                        fields.addAll(List.of(dataSet.getClass().getSuperclass().getDeclaredFields()));
                    }
                    for (final Field field : fields) {
                        field.setAccessible(true);
                        final Object fieldValue = resultSet.getObject(field.getName());
                        field.set(dataSet, fieldValue);
                    }
                }
                return dataSet;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public DBClient getConnection() {
        return dbClient;
    }

    @Override
    public void close() throws Exception {
        dbClient.close();
    }
}
