package com.tide.helper;

public class Entity {
    private Observer observer;

    public void attach(Observer observer) {
        this.observer = observer;
    }

    public void notifyUpdate() {
        this.observer.update();
    }
}
