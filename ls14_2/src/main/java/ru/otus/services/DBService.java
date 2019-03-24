package ru.otus.services;

import java.util.List;

public interface DBService extends AutoCloseable {
    String getLocalStatus();

    long save(User dataSet);

    User read(long id);

    <T> List<T> getAll(Class<T> clazz);
}
