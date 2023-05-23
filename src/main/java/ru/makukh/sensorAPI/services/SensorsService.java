package ru.makukh.sensorAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.makukh.sensorAPI.models.Sensor;
import ru.makukh.sensorAPI.repositories.SensorsRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SensorsService {

    private final SensorsRepository sensorsRepository;

    @Autowired
    public SensorsService(SensorsRepository sensorsRepository) {
        this.sensorsRepository = sensorsRepository;
    }

    public Optional<Sensor> findByName(String name) {
        return sensorsRepository.findByName(name);
    }

    public void save(Sensor sensor) {
        sensor.setCreatedAt(LocalDateTime.now());
        sensorsRepository.save(sensor);
    }
}
