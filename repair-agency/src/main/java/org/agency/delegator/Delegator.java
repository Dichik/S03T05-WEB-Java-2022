package org.agency.delegator;

public interface Delegator<T> {

    T getByClass(Class<?> clazz) throws ClassNotFoundException;

}
