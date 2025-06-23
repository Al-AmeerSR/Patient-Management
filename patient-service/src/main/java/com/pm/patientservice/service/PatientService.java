package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    public List<PatientResponseDTO> getPatients ();

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO);

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO);

    public void deletePatient(UUID id);
}
