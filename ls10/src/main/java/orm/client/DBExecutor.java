package orm.client;

import orm.db.DataSet;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;


public class DBExecutor implements AutoCloseable {
    private DBClient dbClient;
    private Cache cache;

    public DBExecutor() {
        this.dbClient = new DBClient();
        this.cache = new Cache();
    }

    public <DS extends DataSet> void save(final DS dataSet) throws SQLException, IllegalAccessException {
        if (dataSet == null) {
            throw new RuntimeException("Data set can't be null");
        }
        final String insertStatement = "insert into orm (%s) values (%s)";
        final ArrayList<String> fieldNames = new ArrayList<>();
        final ArrayList<String> fieldValues = new ArrayList<>();
        final Set<Field> fields = cache.getFieldsFromClass(dataSet.getClass());
        for (final Field field : fields) {
            field.setAccessible(true);
            fieldNames.add(field.getName());
            fieldValues.add("?");
        }
        final String INSERT = String.format(insertStatement,
                String.join(", ", fieldNames),
                String.join(", ", fieldValues));
        try (final PreparedStatement preparedStatement = dbClient.prepareStatement(INSERT)) {
            int i = 0;
            for (final Field field : fields) {
                i++;
                preparedStatement.setObject(i, field.get(dataSet));
            }
            preparedStatement.executeUpdate();
        }
    }

    public <DS extends DataSet> DS load(final long id, final Class<DS> clazz) {
        if (clazz == null) {
            throw new RuntimeException("Class can't be null");
        } else {
            final String selectStatement = "select * from orm where id=?";
            try (final PreparedStatement preparedStatement = dbClient.prepareStatement(selectStatement)) {
                final DS dataSet = clazz.getConstructor().newInstance();
                preparedStatement.setLong(1, id);
                preparedStatement.executeQuery();
                final ResultSet resultSet = preparedStatement.getResultSet();
                if (resultSet.next()) {
                    for (final Field field : cache.getFieldsFromClass(clazz)) {
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
