package ru.makukh.sensorAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.makukh.sensorAPI.models.Measurement;

import java.util.Optional;

@Repository
public interface MeasurementsRepository extends JpaRepository<Measurement, Integer> {
    long countByRaining(boolean isRaining);
}
