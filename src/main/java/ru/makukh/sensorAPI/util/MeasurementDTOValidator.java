package ru.makukh.sensorAPI.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.makukh.sensorAPI.dto.MeasurementDTO;
import ru.makukh.sensorAPI.services.SensorsService;

@Component
public class MeasurementDTOValidator implements Validator {

    private final SensorsService sensorsService;

    @Autowired
    public MeasurementDTOValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MeasurementDTOValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MeasurementDTO measurementDTO = (MeasurementDTO) target;

        if (sensorsService.findByName(measurementDTO.getSensor().getName())
                .isEmpty()) {
            errors.rejectValue("sensor", "", "This sensor is not registered!");
        }
    }
}
