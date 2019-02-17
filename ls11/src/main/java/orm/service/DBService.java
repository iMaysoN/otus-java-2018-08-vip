package orm.service;

import orm.data.User;

import java.util.List;

public interface DBService extends AutoCloseable {
    String getLocalStatus();

    long save(User dataSet);

    User read(long id);

    User readByName(String name);

    List<User> readAll();
}
