package org.agency.repository;

public interface BaseRepository<T> {

    void findAll();

    void findById(Long id);

    void create(T t);

    T update();

    void delete(Long id);

}
