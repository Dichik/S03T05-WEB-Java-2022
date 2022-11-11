package org.agency.repository;

public interface PersonRepository<T> {

    T findByEmail(String email);

}
