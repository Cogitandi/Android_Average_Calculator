package com.ism.logic;

public class GradeModel {
    private int name;
    private int value;

    public GradeModel(int name) {
        this.name = name;
    }

    public int getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
