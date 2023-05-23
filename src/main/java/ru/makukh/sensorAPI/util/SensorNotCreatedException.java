package ru.makukh.sensorAPI.util;

public class SensorNotCreatedException extends RuntimeException{
    public SensorNotCreatedException(String msg) {
        super(msg);
    }
}
