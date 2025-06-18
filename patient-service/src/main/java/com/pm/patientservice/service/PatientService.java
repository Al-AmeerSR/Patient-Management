package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PatientService {

    private PatientRepository patientRepository;

    public List<PatientResponseDTO>getPatients (){

        List<Patient> patients = patientRepository.findAll();

        return patients.stream()
                        .map(PatientMapper::toDTO)
                        .toList();

    }
}
