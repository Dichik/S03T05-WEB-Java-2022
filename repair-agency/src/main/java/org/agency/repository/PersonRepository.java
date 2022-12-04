package org.agency.repository;

import org.agency.exception.EntityNotFoundException;

import java.util.Optional;

public interface PersonRepository<T> {

    Optional<T> findByEmail(String email) throws EntityNotFoundException;

}
