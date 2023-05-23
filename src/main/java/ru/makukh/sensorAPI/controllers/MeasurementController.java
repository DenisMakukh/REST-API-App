package ru.makukh.sensorAPI.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.makukh.sensorAPI.dto.MeasurementDTO;
import ru.makukh.sensorAPI.dto.MeasurementsResponse;
import ru.makukh.sensorAPI.models.Measurement;
import ru.makukh.sensorAPI.services.MeasurementsService;
import ru.makukh.sensorAPI.services.SensorsService;
import ru.makukh.sensorAPI.util.MeasurementDTOValidator;
import ru.makukh.sensorAPI.util.MeasurementErrorResponse;
import ru.makukh.sensorAPI.util.MeasurementNotAddedException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementsService measurementsService;
    private final ModelMapper modelMapper;
    private final MeasurementDTOValidator measurementDTOValidator;
    private final SensorsService sensorsService;

    @Autowired
    public MeasurementController(MeasurementsService measurementsService, ModelMapper modelMapper, MeasurementDTOValidator measurementDTOValidator, SensorsService sensorsService) {
        this.measurementsService = measurementsService;
        this.modelMapper = modelMapper;
        this.measurementDTOValidator = measurementDTOValidator;
        this.sensorsService = sensorsService;
    }

    @GetMapping()
    public MeasurementsResponse getAllMeasurements(){
        return new MeasurementsResponse(measurementsService.findAll().stream().map(this::convertToMeasurementDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/rainyDaysCount")
    public long rainyDaysCount(){
        return measurementsService.countRainingDays();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                     BindingResult bindingResult) {

        measurementDTOValidator.validate(measurementDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMsg.append(fieldError.getField())
                        .append(" -- ")
                        .append(fieldError.getDefaultMessage())
                        .append(";");
            });

            throw new MeasurementNotAddedException(errorMsg.toString());
        }

        measurementsService.save(convertToMeasurement(measurementDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<MeasurementErrorResponse> handleException(MeasurementNotAddedException e) {
        MeasurementErrorResponse response =
                new MeasurementErrorResponse(
                        e.getMessage(),
                        System.currentTimeMillis()
                );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        Measurement measurement = modelMapper.map(measurementDTO, Measurement.class);
        measurement.setManagedSensor(sensorsService.findByName(measurementDTO.getSensor()
                .getName()).get());
        return measurement;
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }
}
