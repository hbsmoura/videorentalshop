package com.hbsmoura.videorentalshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hbsmoura.videorentalshop.dtos.ClientDto;
import com.hbsmoura.videorentalshop.dtos.ClientLoginDto;
import com.hbsmoura.videorentalshop.model.Client;
import com.hbsmoura.videorentalshop.service.ClientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ClientService clientService;

    private final Client mockedClient = Client.builder()
            .id(UUID.randomUUID())
            .name("Mocked Name")
            .username("mockeduser")
            .password(new BCryptPasswordEncoder().encode("pass"))
            .build();

    private final ClientDto mockedClientDto = new ModelMapper().map(mockedClient, ClientDto.class);

    @Test
    @DisplayName("Create client test")
    @WithMockUser
    void createClientTest() throws Exception {
        ClientLoginDto ClientLoginDto = new ModelMapper().map(mockedClient, ClientLoginDto.class);

        doReturn(ClientLoginDto).when(clientService).createClient(any(ClientDto.class));

        mockMvc
                .perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(mockedClientDto))
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(ClientLoginDto)));
    }

    @Test
    @DisplayName("List clients test")
    @WithMockUser
    void listClientsTest() throws Exception {
        Page<ClientDto> page = new PageImpl<>(Collections.singletonList(mockedClientDto));

        doReturn(page).when(clientService).listClients(any(Pageable.class));

        mockMvc
                .perform(
                        get("/clients")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(page)));
    }

    @Test
    @DisplayName("Get client by id test")
    @WithMockUser
    void getClientByIdTest() throws Exception {

        doReturn(mockedClientDto).when(clientService).getClientById(any(UUID.class));

        mockMvc
                .perform(
                        get("/clients/" + UUID.randomUUID())
                )
                .andExpect(status().isOk())
                .andExpect(content().json(getJsonStringOfClientDtoWithoutLink()));
    }

    @Test
    @DisplayName("Search clients by name or username test")
    @WithMockUser
    void searchClientsByNameOrUsernameTest() throws Exception {
        Page<ClientDto> page = new PageImpl<>(Collections.singletonList(mockedClientDto));

        doReturn(page).when(clientService)
                .searchClientsByNameOrUsername(anyString(), any(Pageable.class));

        mockMvc
                .perform(
                        get("/clients/search/" + mockedClientDto.getUsername())
                )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(page)));
    }

    @Test
    @DisplayName("Update client test")
    @WithMockUser
    void updateClientTest() throws Exception {
        ClientLoginDto clientLoginDto = new ModelMapper().map(mockedClient, ClientLoginDto.class);
        doReturn(clientLoginDto).when(clientService).updateClient(any(ClientLoginDto.class));

        mockMvc
                .perform(
                        put("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(clientLoginDto))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(clientLoginDto)));
    }

    @Test
    @DisplayName("Delete client test")
    @WithMockUser
    void deleteClientTest() throws Exception {
        doNothing().when(clientService).deleteClientById(any(UUID.class));

        mockMvc
                .perform(
                        delete("/clients/" + mockedClientDto.getId())
                )
                .andExpect(status().isNoContent())
                .andExpect(status().reason("Client with given Id successfully deleted"));
    }

    // The Employee DTO object instance without the property "links"
    // to prevent mismatch due to Hateoas support
    private String getJsonStringOfClientDtoWithoutLink() throws JsonProcessingException {
        ObjectNode objectNode = mapper.readTree(mapper.writeValueAsString(mockedClientDto)).deepCopy();
        return objectNode.without("links").toPrettyString();
    }
}
