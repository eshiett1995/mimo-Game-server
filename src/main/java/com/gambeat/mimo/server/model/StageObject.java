package com.gambeat.mimo.server.model;

public class StageObject {
    public enum GameObject{
        Ledge,
        BreakableLedge,
        SpikedLedge,
        SpikedRoller,
    }
    private String object;
    private String item;
    private float coordinate;
    private  boolean hasLife;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public float getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(float coordinate) {
        this.coordinate = coordinate;
    }

    public boolean isHasLife() {
        return hasLife;
    }

    public void setHasLife(boolean hasLife) {
        this.hasLife = hasLife;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
