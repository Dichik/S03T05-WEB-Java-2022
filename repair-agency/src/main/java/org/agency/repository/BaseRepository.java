package org.agency.repository;

public interface BaseRepository<T> {

    void findAll();

    void findById(Long id);

    void create(T t);

    void update(T t);

    void delete(Long id);

}
