package com.pm.patientservice.mapper;

import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.model.Patient;

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
}
