package org.agency.repository;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    void createTable();

    void dropTable();

    List<T> findAll();

    Optional<T> findById(Long id);

    void create(T t);

    void update(Long id, T item);

    void delete(Long id);

}
