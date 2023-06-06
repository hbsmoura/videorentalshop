package com.hbsmoura.videorentalshop.dtos;

import com.hbsmoura.videorentalshop.config.hateoas.HateoasIdentity;
import com.hbsmoura.videorentalshop.config.hateoas.HateoasModel;
import com.hbsmoura.videorentalshop.controller.EmployeeController;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@HateoasModel(controller = EmployeeController.class)
public class EmployeeDto extends RepresentationModel<EmployeeDto> implements Serializable {
    private static final long serialVersionUID = 1L;

    @HateoasIdentity
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    private boolean manager;
}
