package com.hbsmoura.videorentalshop.service;

import com.hbsmoura.videorentalshop.dtos.EmployeeDto;
import com.hbsmoura.videorentalshop.dtos.EmployeeLoginDto;
import com.hbsmoura.videorentalshop.exceptions.EmployeeNotFoundException;
import com.hbsmoura.videorentalshop.model.Employee;
import com.hbsmoura.videorentalshop.repository.EmployeeRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Method for insertion of a new employee.
     * Attaches a random alphanumeric password for the given employee
     * @param givenEmployee the employee to be created
     * @return the created employee with attached random password
     */

    public EmployeeLoginDto createEmployee(EmployeeDto givenEmployee) {
        String randomPass = RandomStringUtils.randomAlphanumeric(12);
        Employee newEmployee = Employee.builder()
                .name(givenEmployee.getName())
                .username(givenEmployee.getUsername())
                .password(passwordEncoder.encode(randomPass))
                .manager(givenEmployee.isManager())
                .build();

        // Only for test
//        System.out.println("Employee: " + newEmployee.getUsername() + " - Password: " + randomPass);

        Employee savedEmployee = employeeRepository.save(newEmployee);

        EmployeeLoginDto employeeToBeReturned = new EmployeeLoginDto(savedEmployee);
        employeeToBeReturned.setPassword(randomPass);

        return employeeToBeReturned;
    }

    /**
     * Method for get a page of employees with the given properties.
     * @param pageable the object that carries the page properties
     * @return a page of employees from the model layer
     */

    public Page<EmployeeDto> listEmployees(Pageable pageable) {
        Page<Employee> employees = employeeRepository.findAll(pageable);
        return employees.map(employee -> new ModelMapper().map(employee, EmployeeDto.class));
    }

    /**
     * Method for retrieve an employee by it's id.
     * @param id the given id
     * @return the found employee
     * @throws EmployeeNotFoundException if there is no employee with the given id on the model layer
     */

    public EmployeeDto getEmployeeById(UUID id) {
        Employee employee =  employeeRepository.findById(id).orElseThrow(EmployeeNotFoundException::new);
        return new ModelMapper().map(employee, EmployeeDto.class);
    }

    /**
     * Method for search employees by it's name or username.
     * @param text the text for the search
     * @param pageable the object that carries the page properties
     * @return a page of found employees from the model layer
     */

    public Page<EmployeeDto> searchEmployeesByNameOrUsername(String text, Pageable pageable) {
        Page<Employee> employees = employeeRepository.findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(text, text, pageable);
        return employees.map(employee -> new ModelMapper().map(employee, EmployeeDto.class));
    }

    /**
     * Method for update a given employee.
     * @param givenEmployee the employee to be updated
     * @return the updated employee
     * @throws EmployeeNotFoundException if there is no employee with the id of the given employee on the model layer
     */

    public EmployeeLoginDto updateEmployee(EmployeeLoginDto givenEmployee) {
        Employee employee = employeeRepository.findById(givenEmployee.getId()).orElseThrow(EmployeeNotFoundException::new);

        employee.setName(givenEmployee.getName());
        employee.setUsername(givenEmployee.getUsername());
        employee.setManager(givenEmployee.isManager());

        employeeRepository.save(employee);

        return new ModelMapper().map(employee, EmployeeLoginDto.class);
    }

    /**
     * Method for delete an employee by a given id.
     * @param id the given id
     * @throws EmployeeNotFoundException if there is no employee with the given id on the model layer
     */

    public void deleteEmployeeById(UUID id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(EmployeeNotFoundException::new);
        employeeRepository.delete(employee);
    }
}
