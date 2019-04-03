package com.example.pi4homemobile.model;

public class Light {
    private String name;
    private boolean turnedOn;

    public void setName(String name) {
        this.name = name;
    }

    public void setTurnedOn(boolean turnedOn) {
        this.turnedOn = turnedOn;
    }

    public boolean isTurnedOn() {
        return turnedOn;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Light{" +
                "name='" + name + '\'' +
                ", turnedOn=" + turnedOn +
                '}';
    }
}
