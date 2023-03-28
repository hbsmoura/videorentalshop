package com.hbsmoura.videorentalshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hbsmoura.videorentalshop.config.security.FakeSecurityConfig;
import com.hbsmoura.videorentalshop.config.security.JwtAuthenticationFilter;
import com.hbsmoura.videorentalshop.dtos.ChangePasswordDto;
import com.hbsmoura.videorentalshop.dtos.EmployeeDto;
import com.hbsmoura.videorentalshop.dtos.EmployeeLoginDto;
import com.hbsmoura.videorentalshop.model.Employee;
import com.hbsmoura.videorentalshop.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(
        value = EmployeeController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@Import({FakeSecurityConfig.class})
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmployeeService employeeService;

    private Employee mockedEmployee;
    private EmployeeDto mockedEmployeeDto;

    @BeforeEach
    public void setup() {
        mockedEmployee = Employee.builder()
                .id(UUID.fromString("7b8e6d52-9534-45d6-a8f9-43c41082d7ff"))
                .name("Mocked Employee")
                .username("mockedemployee")
                .password(passwordEncoder.encode("pass"))
                .manager(false)
                .build();

        mockedEmployeeDto = new ModelMapper().map(mockedEmployee, EmployeeDto.class);
    }

    @Test
    @DisplayName("Create employee test")
    @WithMockUser(roles = "MANAGER")
    void createEmployeeTest() throws Exception {
        EmployeeLoginDto employeeLoginDto = new ModelMapper().map(mockedEmployee, EmployeeLoginDto.class);

        doReturn(employeeLoginDto).when(employeeService).createEmployee(any(EmployeeDto.class));

        mockMvc
                .perform(
                        post("/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(mockedEmployeeDto))
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("List employees test")
    @WithMockUser(roles = "MANAGER")
    void listEmployeesTest() throws Exception {
        Page<EmployeeDto> page = new PageImpl<>(Collections.singletonList(mockedEmployeeDto));

        doReturn(page).when(employeeService).listEmployees(any(Pageable.class));

        mockMvc
                .perform(
                        get("/employees")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(page)));
    }

    @Test
    @DisplayName("Get employee by id test")
    @WithMockUser(roles = "MANAGER")
    void getEmployeeByIdTest() throws Exception {

        doReturn(mockedEmployeeDto).when(employeeService).getEmployeeById(any(UUID.class));

        mockMvc
                .perform(
                        get("/employees/" + UUID.randomUUID())
                )
                .andExpect(status().isOk())
                .andExpect(content().json(getJsonStringOfEmployeeDtoWithoutLink()));
    }

    @Test
    @DisplayName("Search by name or username test")
    @WithMockUser(roles = "MANAGER")
    void searchEmployeesByNameOrUsernameTest() throws Exception {
        Page<EmployeeDto> page = new PageImpl<>(Collections.singletonList(mockedEmployeeDto));

        doReturn(page).when(employeeService)
                .searchEmployeesByNameOrUsername(anyString(), any(Pageable.class));

        mockMvc
                .perform(
                        get("/employees/search/" + mockedEmployeeDto.getUsername())
                )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(page)));
    }

    @Test
    @DisplayName("Update employee test")
    @WithUserDetails(userDetailsServiceBeanName = "userService", value = "mockedemployee")
    void updateEmployeeTest() throws Exception {
        EmployeeDto mockedEmployeeDto = new ModelMapper().map(mockedEmployee, EmployeeDto.class);

        doReturn(mockedEmployeeDto).when(employeeService).updateEmployee(any(EmployeeLoginDto.class));

        mockMvc
                .perform(
                        put("/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(mockedEmployeeDto))
                                .with(user(mockedEmployee))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Set management test")
    @WithMockUser(roles = "MANAGER")
    void setManagementTest() throws Exception {
        EmployeeDto mockedEmployeeDto = new ModelMapper().map(mockedEmployee, EmployeeDto.class);

        doReturn(mockedEmployeeDto).when(employeeService).setManagement(any(UUID.class), anyBoolean());

        mockMvc
                .perform(
                        patch("/employees/"+ mockedEmployeeDto.getId()
                                + "/management/" + false)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(getJsonStringOfEmployeeDtoWithoutLink()));
    }

    @Test
    @DisplayName("Change password test")
    @WithUserDetails(value = "mockedemployee", userDetailsServiceBeanName = "userService")
    void changePasswordTest() throws Exception {
        doNothing().when(employeeService).changePassword(any(UUID.class), any(ChangePasswordDto.class));

        mockMvc
                .perform(
                        patch("/employees/"+ mockedEmployee.getId() +"/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        mapper.writeValueAsString(
                                                ChangePasswordDto.builder()
                                                        .build()
                                        )
                                )
                )
                .andExpect(status().isNoContent())
                .andExpect(status().reason("Password successfully changed"));
    }

    @Test
    @DisplayName("Delete employee test")
    @WithMockUser(roles = "MANAGER")
    void deleteEmployeeTest() throws Exception {
        doNothing().when(employeeService).deleteEmployeeById(any(UUID.class));

        mockMvc
                .perform(
                        delete("/employees/" + mockedEmployeeDto.getId())
                )
                .andExpect(status().isNoContent())
                .andExpect(status().reason("Employee with given Id successfully deleted"));
    }

    // The Employee DTO object instance without the property "links"
    // to prevent mismatch, due to Hateoas support
    private String getJsonStringOfEmployeeDtoWithoutLink() throws JsonProcessingException {
        ObjectNode objectNode = mapper.readTree(mapper.writeValueAsString(mockedEmployeeDto)).deepCopy();
        return objectNode.without("links").toPrettyString();
    }
}
