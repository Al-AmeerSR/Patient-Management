package com.pm.patientservice.kafka;

import com.pm.patientservice.model.Patient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@AllArgsConstructor
@Service
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private KafkaTemplate<String, byte[]> kafkaTemplate; // this how we define message types and use kafkaTemplate to send message

    public void sendEvent(Patient patient) {

        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();

        try{

            kafkaTemplate.send("patient", event.toByteArray());

        }catch(Exception e){

            log.error("Error sending event : {}", event);
        }
    }
}
