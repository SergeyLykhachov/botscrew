package com.yahoo.slykhachov.botscrew.util;

public interface Subject {
    void registerObserver(Observer o);
    void notifyObserver();
}
