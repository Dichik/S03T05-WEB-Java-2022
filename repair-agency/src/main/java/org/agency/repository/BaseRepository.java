package org.agency.repository;

import java.util.List;

public interface BaseRepository<T> {

    void createTable();

    void dropTable();

    List<T> findAll();

    T findById(Long id);

    void create(T t);

    void update(T t);

    void delete(Long id);

}
