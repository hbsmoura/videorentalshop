package com.hbsmoura.videorentalshop.dtos;

import com.hbsmoura.videorentalshop.model.Employee;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeLoginDto {

    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    private String password;

    private boolean isManager;

    public EmployeeLoginDto(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.username = employee.getUsername();
        this.isManager = employee.isManager();
    }
}
