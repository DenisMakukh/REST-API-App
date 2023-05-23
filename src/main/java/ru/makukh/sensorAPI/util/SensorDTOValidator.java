package ru.makukh.sensorAPI.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.makukh.sensorAPI.dto.SensorDTO;
import ru.makukh.sensorAPI.services.SensorsService;

@Component
public class SensorDTOValidator implements Validator {

    private final SensorsService sensorsService;

    @Autowired
    public SensorDTOValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return SensorDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SensorDTO sensor = (SensorDTO) target;

        if (sensorsService.findByName(sensor.getName()).isPresent()) {
            errors.rejectValue("name", "", "Name is already used");
        }
    }
}
