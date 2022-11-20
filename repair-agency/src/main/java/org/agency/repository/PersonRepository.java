package org.agency.repository;

import org.agency.exception.EntityNotFoundException;

public interface PersonRepository<T> {

    T findByEmail(String email) throws EntityNotFoundException;

}
