package com.videorental.observer;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private final List<EventObserver> observers = new ArrayList<>();

    public void subscribe(EventObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(EventObserver observer) {
        observers.remove(observer);
    }

    public void notify(RentalEvent event) {
        for (EventObserver observer : observers) {
            observer.onEvent(event);
        }
    }
}
