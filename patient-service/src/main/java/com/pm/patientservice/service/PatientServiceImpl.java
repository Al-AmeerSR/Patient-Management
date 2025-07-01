package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.kafka.KafkaProducer;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;
    private BillingServiceGrpcClient billingServiceGrpcClient;
    private KafkaProducer kafkaProducer;

    @Override
    @Cacheable(value = "patients")
    public List<PatientResponseDTO>getPatients (){

        List<Patient> patients = patientRepository.findAll();

        return patients.stream()
                        .map(PatientMapper::toDTO)
                        .toList();

    }
    @Override
    @CacheEvict(value = "patients", allEntries = true)
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){

        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){

            throw new EmailAlreadyExistException("A patient with this email already exists "+patientRequestDTO.getEmail());
        }
        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(),
                newPatient.getName(),
                newPatient.getEmail());

        kafkaProducer.sendEvent(newPatient);

        return PatientMapper.toDTO(newPatient);

    }
    @Override
    @CacheEvict(value = "patients", allEntries = true)
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO){

    Patient patient = patientRepository.findById(id).orElseThrow(()-> new PatientNotFoundException("Patient not found with id :"+id));

    if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id)){

        throw new EmailAlreadyExistException("A patient with this email already exists "+patientRequestDTO.getEmail());
    }
        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

    Patient updatedPatient = patientRepository.save(patient);
    return PatientMapper.toDTO(updatedPatient);

    }

    @Override
    @CacheEvict(value = "patients", allEntries = true)
    public void deletePatient(UUID id){

        patientRepository.deleteById(id);

    }

}
