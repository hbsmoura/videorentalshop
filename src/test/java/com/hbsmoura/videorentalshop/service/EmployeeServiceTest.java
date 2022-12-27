package com.hbsmoura.videorentalshop.service;

import com.hbsmoura.videorentalshop.dtos.EmployeeDto;
import com.hbsmoura.videorentalshop.dtos.EmployeeLoginDto;
import com.hbsmoura.videorentalshop.exceptions.EmployeeNotFoundException;
import com.hbsmoura.videorentalshop.model.Employee;
import com.hbsmoura.videorentalshop.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private Employee mockedEmployee = Employee.builder()
            .id(UUID.randomUUID())
            .name("Mocked Name")
            .username("mockeduser")
            .password(bCryptPasswordEncoder.encode("pass"))
            .manager(false)
            .build();

    private Page<Employee> mockedPageEmployees = new PageImpl<>(Collections.singletonList(mockedEmployee));

    private EmployeeDto mockedEmployeeDto = new ModelMapper().map(mockedEmployee, EmployeeDto.class);

    private EmployeeLoginDto mockedEmployeeLoginDto = new ModelMapper().map(mockedEmployee, EmployeeLoginDto.class);

    @Test
    @DisplayName("Create employee test")
    void createEmployeeTest() {
        doReturn(mockedEmployee.getPassword()).when(passwordEncoder).encode(anyString());
        doReturn(mockedEmployee).when(employeeRepository).save(any(Employee.class));

        EmployeeLoginDto returnedEmployee = employeeService.createEmployee(mockedEmployeeDto);

        assertThat(returnedEmployee.getId(), is(mockedEmployee.getId()));
        assertThat(returnedEmployee.getName(), is(mockedEmployee.getName()));
        assertFalse(returnedEmployee.isManager());
    }

    @Test
    @DisplayName("Paging list of employees test")
    void listEmployeesTest() {
        doReturn(mockedPageEmployees).when(employeeRepository).findAll(any(Pageable.class));

        Page<EmployeeDto> returnedPage = employeeService.listEmployees(PageRequest.ofSize(mockedPageEmployees.getSize()));

        assertThat(returnedPage.getTotalElements(), is(1L));
        assertThat(returnedPage.getContent().get(0), is(mockedEmployeeDto));
    }

    @Test
    @DisplayName("Find employee by the given id test")
    void getEmployeeById() {
        doReturn(Optional.of(mockedEmployee)).when(employeeRepository).findById(any(UUID.class));

        EmployeeDto returnedEmployee = employeeService.getEmployeeById(UUID.randomUUID());

        assertThat(returnedEmployee.getId(), is(mockedEmployee.getId()));
        assertThat(returnedEmployee.getName(), is(mockedEmployee.getName()));
        assertThat(returnedEmployee.getUsername(), is(mockedEmployee.getUsername()));
        assertThat(returnedEmployee.isManager(), is(mockedEmployee.isManager()));
    }

    @Test
    @DisplayName("Find employee by the given id throw exception test")
    void getEmployeeByIdThrowExceptionTest() {
        doReturn(Optional.empty()).when(employeeRepository).findById(any(UUID.class));

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Search employee by name or username test")
    void searchEmployeeByNameOrUsernameTest() {
        doReturn(mockedPageEmployees).when(employeeRepository)
                .findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(
                        anyString(), anyString(), any(Pageable.class)
                );

        Page<EmployeeDto> returnedPage =
                employeeService.searchEmployeesByNameOrUsername("Mocked", mockedPageEmployees.getPageable());

        assertThat(returnedPage.getTotalElements(), is(1L));
        assertThat(returnedPage.getContent().get(0), is(mockedEmployeeDto));
    }

    @Test
    @DisplayName("Update employee test")
    void updateEmployeeTest() {
        doReturn(Optional.of(mockedEmployee)).when(employeeRepository).findById(any(UUID.class));
        doReturn(mockedEmployee).when(employeeRepository).save(any(Employee.class));

        EmployeeLoginDto returnedEmployeeDto = employeeService.updateEmployee(mockedEmployeeLoginDto);

        assertThat(returnedEmployeeDto.getId(), is(mockedEmployee.getId()));
        assertThat(returnedEmployeeDto.getName(), is(mockedEmployee.getName()));
        assertThat(returnedEmployeeDto.getUsername(), is(mockedEmployee.getUsername()));
        assertThat(returnedEmployeeDto.isManager(), is(mockedEmployee.isManager()));
    }

    @Test
    @DisplayName("Update employee throw exception test")
    void updateEmployeeThrowExcpetionTest() {
        doReturn(Optional.empty()).when(employeeRepository).findById(any(UUID.class));

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(mockedEmployeeLoginDto));
    }

    @Test
    @DisplayName("Delete employee by id test")
    void deleteEmployeeById() {
        doReturn(Optional.of(mockedEmployee)).when(employeeRepository).findById(any(UUID.class));
        doNothing().when(employeeRepository).delete(any(Employee.class));

        employeeService.deleteEmployeeById(UUID.randomUUID());

        verify(employeeRepository, times(1)).findById(any(UUID.class));
        verify(employeeRepository, times(1)).delete(any(Employee.class));
    }

    @Test
    @DisplayName("Delete employee by id throw excpetion test")
    void deleteEmployeeByIdThrowException() {
        doReturn(Optional.empty()).when(employeeRepository).findById(any(UUID.class));

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployeeById(UUID.randomUUID()));
    }
}
