package com.hbsmoura.videorentalshop.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDto extends RepresentationModel<EmployeeDto> {

    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    private boolean manager;
}
