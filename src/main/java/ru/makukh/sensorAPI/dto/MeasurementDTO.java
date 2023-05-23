package ru.makukh.sensorAPI.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MeasurementDTO {

    @Min(value = -100)
    @Max(value = 100)
    @NotNull(message = "Value should not be empty")
    private Double value;

    @NotNull(message = "Is raining should not be empty")
    private Boolean raining;

    @NotNull(message = "Sensor should not be empty")
    private SensorDTO managedSensor;

    public Double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return managedSensor;
    }

    public void setSensor(SensorDTO managedSensor) {
        this.managedSensor = managedSensor;
    }
}
