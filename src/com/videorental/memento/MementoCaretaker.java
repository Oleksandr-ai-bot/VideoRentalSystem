package com.videorental.memento;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class MementoCaretaker {
    private final Deque<SystemMemento> history = new ArrayDeque<>();

    public void save(SystemMemento memento) {
        history.push(memento);
    }

    public Optional<SystemMemento> restore() {
        return history.isEmpty() ? Optional.empty() : Optional.of(history.pop());
    }

    public boolean hasState() {
        return !history.isEmpty();
    }

    public int getHistorySize() {
        return history.size();
    }
}
