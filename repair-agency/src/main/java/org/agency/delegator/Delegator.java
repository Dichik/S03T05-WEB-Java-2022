package org.agency.delegator;

public interface Delegator<T> {

    boolean existsByClass(Class<?> clazz);

    T getByClass(Class<?> clazz);

}
