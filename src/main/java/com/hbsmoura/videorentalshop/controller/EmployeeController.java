package com.hbsmoura.videorentalshop.controller;

import com.hbsmoura.videorentalshop.dtos.EmployeeDto;
import com.hbsmoura.videorentalshop.dtos.EmployeeLoginDto;
import com.hbsmoura.videorentalshop.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeLoginDto createEmployee(@RequestBody EmployeeDto givenEmployee) {
        return employeeService.createEmployee(givenEmployee);
    }

    @GetMapping
    public Page<EmployeeDto> listEmployees(Pageable pageable) {
        return employeeService.listEmployees(pageable);
    }

    @GetMapping("/{id}")
    public EmployeeDto getEmployeeById(@PathVariable("id") UUID id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/search/{text}")
    public Page<EmployeeDto> searchEmployeesByNameOrUsername(@PathVariable("text") String text, Pageable pageable) {
        return employeeService.searchEmployeesByNameOrUsername(text, pageable);
    }

    @PutMapping
    public EmployeeLoginDto updateEmployee(@RequestBody EmployeeLoginDto givenEmployee) {
        return employeeService.updateEmployee(givenEmployee);
    }

    @PatchMapping("/{id}/management/{set}")
    public EmployeeDto setManagement(@PathVariable("id") UUID id, @PathVariable("set") boolean set) {
        return employeeService.setManagement(id, set);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Employee with given Id successfully deleted")
    public void deleteEmployee(@PathVariable("id") UUID id) {
        employeeService.deleteEmployeeById(id);
    }
}
