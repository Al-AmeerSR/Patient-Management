package com.pm.patientservice.dto;

import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Name is required")
    @Size(max = 100,message="name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message ="Email should be valid" )
    private String email;

    @NotBlank(message="Address is Required")
    private String address;

    @NotBlank(message = "Date of birth is required")
    private String dateOfBirth;

    @NotBlank(groups = CreatePatientValidationGroup.class,message="Registered date is required")
    private String registeredDate;
}
