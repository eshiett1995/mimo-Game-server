package com.gambeat.mimo.server.model;

public class StageObject {
    public enum GameObject{
        Ledge,
        BreakableLedge,
        SpikedLedge,
        SpikedRoller,
    }
    private GameObject object;
    private double coordinate;
    private  boolean hasLife;

    public GameObject getObject() {
        return object;
    }

    public void setObject(GameObject object) {
        this.object = object;
    }

    public double getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(double coordinate) {
        this.coordinate = coordinate;
    }

    public boolean isHasLife() {
        return hasLife;
    }

    public void setHasLife(boolean hasLife) {
        this.hasLife = hasLife;
    }
}
