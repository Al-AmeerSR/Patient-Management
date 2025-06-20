package com.pm.patientservice.mapper;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.model.Patient;

import java.time.LocalDate;

public class PatientMapper {

    public static PatientResponseDTO toDTO(Patient patient){

        PatientResponseDTO patientDTO;
        patientDTO = PatientResponseDTO.builder()
                                        .id(patient.getId().toString())
                                        .name(patient.getName())
                                        .address(patient.getAddress())
                                        .email(patient.getEmail())
                                        .dateOfBirth(patient.getDateOfBirth().toString())
                                        .build();
        return patientDTO;

    }

    public static Patient toModel(PatientRequestDTO patientRequestDTO){

        Patient patient ;
        patient = Patient.builder()
                .name(patientRequestDTO.getName())
                .address(patientRequestDTO.getAddress())
                .email(patientRequestDTO.getEmail())
                .dateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()))
                .registeredDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()))
                .build();
        return patient;
    }
}
