package ru.makukh.sensorAPI.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.makukh.sensorAPI.dto.SensorDTO;
import ru.makukh.sensorAPI.models.Sensor;
import ru.makukh.sensorAPI.services.SensorsService;
import ru.makukh.sensorAPI.util.SensorDTOValidator;
import ru.makukh.sensorAPI.util.SensorErrorResponse;
import ru.makukh.sensorAPI.util.SensorNotCreatedException;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorsService sensorsService;
    private final ModelMapper modelMapper;
    private final SensorDTOValidator sensorDTOValidator;

    @Autowired
    public SensorController(SensorsService sensorsService, ModelMapper modelMapper, SensorDTOValidator sensorDTOValidator) {
        this.sensorsService = sensorsService;
        this.modelMapper = modelMapper;
        this.sensorDTOValidator = sensorDTOValidator;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registerSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                     BindingResult bindingResult) {

        sensorDTOValidator.validate(sensorDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            bindingResult.getFieldErrors()
                    .forEach(fieldError -> {
                        errorMsg.append(fieldError.getField())
                                .append(" -- ")
                                .append(fieldError.getDefaultMessage())
                                .append(";");
                    });

            throw new SensorNotCreatedException(errorMsg.toString());
        }

        sensorsService.save(convertToSensor(sensorDTO));

        return ResponseEntity.ok(HttpStatus.OK);

    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotCreatedException e) {
        SensorErrorResponse sensorErrorResponse = new SensorErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(sensorErrorResponse, HttpStatus.BAD_REQUEST);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    private SensorDTO convertToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }
}
