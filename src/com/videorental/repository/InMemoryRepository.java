package com.videorental.repository;

import com.videorental.common.Identifiable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class InMemoryRepository<T extends Identifiable> implements Repository<T> {
    protected final Map<String, T> store = new LinkedHashMap<>();

    @Override
    public void add(T entity) {
        store.put(entity.getId(), entity);
    }

    @Override
    public void remove(String id) {
        store.remove(id);
    }

    @Override
    public T findById(String id) {
        return store.get(id);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void clear() {
        store.clear();
    }
}
