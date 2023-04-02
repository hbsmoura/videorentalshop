package com.hbsmoura.videorentalshop.controller;

import com.hbsmoura.videorentalshop.config.apiresponse.ApiResponseNotFound;
import com.hbsmoura.videorentalshop.config.apiresponse.ApiResponseOk;
import com.hbsmoura.videorentalshop.config.apiresponse.ApiResponseUnauthorized;
import com.hbsmoura.videorentalshop.config.exceptionhandling.ApiErrorResponse;
import com.hbsmoura.videorentalshop.dtos.ChangePasswordDto;
import com.hbsmoura.videorentalshop.dtos.EmployeeDto;
import com.hbsmoura.videorentalshop.dtos.EmployeeLoginDto;
import com.hbsmoura.videorentalshop.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/employees")
@PreAuthorize("hasRole('MANAGER')")
@Tag(name = "Employee Controller")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create employee",
            description = "Creates a new employee, saves on database and retrieves it"
    )
    public EmployeeLoginDto createEmployee(@RequestBody @Valid EmployeeDto givenEmployee) {
        return employeeService.createEmployee(givenEmployee);
    }

    @GetMapping
    @Operation(
            summary = "List employees",
            description = "Retrieves a paged list of employees"
    )
    public Page<EmployeeDto> listEmployees(Pageable pageable) {
        return employeeService.listEmployees(pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get employee by id",
            description = "Retrieves a employee by its id"
    )
    @ApiResponseOk(content = @Content(schema = @Schema(implementation = EmployeeDto.class)))
    @ApiResponseNotFound(content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public EmployeeDto getEmployeeById(@PathVariable UUID id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/search/{text}")
    @Operation(
            summary = "Search employees by name or username",
            description = "Retrieves a list of employees according to the given parameter text"
    )
    public Page<EmployeeDto> searchEmployeesByNameOrUsername(@PathVariable("text") String text, Pageable pageable) {
        return employeeService.searchEmployeesByNameOrUsername(text, pageable);
    }

    @PutMapping
    @PreAuthorize("hasRole('EMPLOYEE') and @authService.isItself(#givenEmployee.id)")
    @Operation(
            summary = "Update employee",
            description = "Updates the employee data"
    )
    @ApiResponseOk(content = @Content(schema = @Schema(implementation = EmployeeDto.class)))
    @ApiResponseNotFound(content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public EmployeeDto updateEmployee(@RequestBody @Valid EmployeeLoginDto givenEmployee) {
        return employeeService.updateEmployee(givenEmployee);
    }

    @PatchMapping("/{id}/management/{set}")
    @Operation(
            summary = "Set management",
            description = "Set the management on the employee with the given id"
    )
    @ApiResponseOk(content = @Content(schema = @Schema(implementation = EmployeeDto.class)))
    @ApiResponseNotFound(content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public EmployeeDto setManagement(@PathVariable UUID id, @PathVariable("set") boolean set) {
        return employeeService.setManagement(id, set);
    }

    @PatchMapping("/{id}/password")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Password successfully changed")
    @PreAuthorize("hasRole('EMPLOYEE') and @authService.isItself(#id)")
    @Operation(
            summary = "Change password",
            description = "Updates the employee's password"
    )
    @ApiResponseNotFound(content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponseUnauthorized(content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public void changePassword(@PathVariable UUID id, ChangePasswordDto changePasswordDto) {
        employeeService.changePassword(id, changePasswordDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Employee with given Id successfully deleted")
    @Operation(
            summary = "Delete client",
            description = "Deletes a employee's from database"
    )
    @ApiResponseNotFound(content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public void deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployeeById(id);
    }
}
