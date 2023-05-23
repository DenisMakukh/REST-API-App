package ru.makukh.sensorAPI.util;

public class MeasurementNotAddedException extends RuntimeException {
    public MeasurementNotAddedException(String msg) {
        super(msg);
    }
}
