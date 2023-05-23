package ru.makukh.sensorAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.makukh.sensorAPI.models.Measurement;
import ru.makukh.sensorAPI.repositories.MeasurementsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeasurementsService {

    private final MeasurementsRepository measurementsRepository;

    @Autowired
    public MeasurementsService(MeasurementsRepository measurementsRepository) {
        this.measurementsRepository = measurementsRepository;
    }

    public void save(Measurement measurement) {
        measurement.setMeasurementDateTime(LocalDateTime.now());
        measurementsRepository.save(measurement);
    }

    public List<Measurement> findAll() {
        return measurementsRepository.findAll();
    }

    public long countRainingDays(){
        return measurementsRepository.countByRaining(true);
    }
}
