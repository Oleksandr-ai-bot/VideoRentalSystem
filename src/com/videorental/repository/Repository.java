package com.videorental.repository;

import com.videorental.common.Identifiable;

import java.util.List;

public interface Repository<T extends Identifiable> {
    void add(T entity);
    void remove(String id);
    T findById(String id);
    List<T> findAll();
    void clear();
}
