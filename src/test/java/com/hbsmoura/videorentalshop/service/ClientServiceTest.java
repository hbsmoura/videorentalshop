package com.hbsmoura.videorentalshop.service;

import com.hbsmoura.videorentalshop.dtos.ChangePasswordDto;
import com.hbsmoura.videorentalshop.dtos.ClientDto;
import com.hbsmoura.videorentalshop.dtos.ClientLoginDto;
import com.hbsmoura.videorentalshop.exceptions.ClientNotFoundException;
import com.hbsmoura.videorentalshop.exceptions.PasswordNotMachException;
import com.hbsmoura.videorentalshop.model.Client;
import com.hbsmoura.videorentalshop.repository.ClientRepository;
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
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private Client mockedClient = Client.builder()
            .id(UUID.randomUUID())
            .name("Mocked Name")
            .username("mockeduser")
            .bookings(new ArrayList<>())
            .password(bCryptPasswordEncoder.encode("pass"))
            .build();

    private Page<Client> mockedPageClients = new PageImpl<>(Collections.singletonList(mockedClient));

    @Test
    @DisplayName("Create client test")
    void createClientTest() {
        doReturn(mockedClient.getPassword()).when(passwordEncoder).encode(anyString());
        doReturn(mockedClient).when(clientRepository).save(any(Client.class));

        ClientLoginDto clientLogin = clientService.createClient(
                new ModelMapper().map(mockedClient, ClientDto.class)
        );

        assertThat(clientLogin.getId(), is(mockedClient.getId()));
        assertThat(clientLogin.getName(), is(mockedClient.getName()));
        assertThat(clientLogin.getUsername(), is(mockedClient.getUsername()));
        assertTrue(bCryptPasswordEncoder.matches("pass", mockedClient.getPassword()));
        assertThat(clientLogin.getBookings(), empty());
    }

    @Test
    @DisplayName("Paging list of clients test")
    void listClientsTest() {
        ClientDto mockedClientDto = new ModelMapper().map(mockedClient, ClientDto.class);

        doReturn(mockedPageClients).when(clientRepository).findAll(any(Pageable.class));

        Page<ClientDto> returnedPage = clientService.listClients(PageRequest.ofSize(mockedPageClients.getSize()));

        assertThat(returnedPage.getTotalElements(), is(1L));
        assertThat(returnedPage.getContent().get(0), is(mockedClientDto));
    }

    @Test
    @DisplayName("Find client by the given id test")
    void getClientById() {
        doReturn(Optional.of(mockedClient)).when(clientRepository).findById(any(UUID.class));

        ClientDto returnedClientDto = clientService.getClientById(UUID.randomUUID());

        assertThat(returnedClientDto.getId(), is(mockedClient.getId()));
        assertThat(returnedClientDto.getName(), is(mockedClient.getName()));
        assertThat(returnedClientDto.getUsername(), is(mockedClient.getUsername()));
        assertThat(returnedClientDto.getBookings(), is(mockedClient.getBookings()));
    }

    @Test
    @DisplayName("Find client by the given id throw exception test")
    void getClientByIdThrowExceptionTest() {
        doReturn(Optional.empty()).when(clientRepository).findById(any(UUID.class));

        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Search client by name or username test")
    void searchClientByNameOrUsernameTest() {
        ClientDto mockedClientDto = new ModelMapper().map(mockedClient, ClientDto.class);

        doReturn(mockedPageClients).when(clientRepository)
                .findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(
                        anyString(), anyString(), any(Pageable.class)
                );

        Page<ClientDto> returnedPage =
                clientService.searchClientsByNameOrUsername("Moked", mockedPageClients.getPageable());

        assertThat(returnedPage.getTotalElements(), is(1L));
        assertThat(returnedPage.getContent().get(0), is(mockedClientDto));
    }

    @Test
    @DisplayName("Update client test")
    void updateClientTest() {
        ClientLoginDto mockedClientLoginDto = new ModelMapper().map(mockedClient, ClientLoginDto.class);

        doReturn(Optional.of(mockedClient)).when(clientRepository).findById(any(UUID.class));
        doReturn(mockedClient).when(clientRepository).save(any(Client.class));

        ClientLoginDto returnedClient = clientService.updateClient(mockedClientLoginDto);

        assertThat(returnedClient.getId(), is(mockedClient.getId()));
        assertThat(returnedClient.getName(), is(mockedClient.getName()));
        assertThat(returnedClient.getUsername(), is(mockedClient.getUsername()));
        assertTrue(bCryptPasswordEncoder.matches("pass", mockedClient.getPassword()));
        assertThat(returnedClient.getBookings(), is(mockedClient.getBookings()));
    }

    @Test
    @DisplayName("Update client throw exception test")
    void updateClientThrowExceptionTest() {
        ClientLoginDto mockedClientLoginDto = new ModelMapper().map(mockedClient, ClientLoginDto.class);

        doReturn(Optional.empty()).when(clientRepository).findById(any(UUID.class));

        assertThrows(ClientNotFoundException.class, () -> clientService.updateClient(mockedClientLoginDto));
    }

    @Test
    @DisplayName("Chane password test")
    void changePasswordTest() {
        doReturn(Optional.of(mockedClient)).when(clientRepository).findById(any(UUID.class));
        doReturn(true).when(passwordEncoder).matches(anyString(), anyString());
        doReturn(mockedClient).when(clientRepository).save(any(Client.class));

        clientService.changePassword(
                mockedClient.getId(),
                ChangePasswordDto.builder()
                        .currentPassword("someOldPassword")
                        .newPassword("someNewPassword")
                        .build()
        );

        verify(clientRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    @DisplayName("Change password throw ClientNotFoundException test")
    void changePasswordThrowClientNotFoundExceptionTest() {
        assertThrows(
                ClientNotFoundException.class,
                () -> clientService.changePassword(null, ChangePasswordDto.builder().build())
        );
    }

    @Test
    @DisplayName("Change password throw PasswordNotMachException test")
    void changePasswordThrowPasswordNotMachExceptionExceptionTest() {
        doReturn(Optional.of(mockedClient)).when(clientRepository).findById(any(UUID.class));

        assertThrows(
                PasswordNotMachException.class,
                () -> clientService.changePassword(
                        UUID.randomUUID(),
                        ChangePasswordDto.builder().build()
                )
        );
    }

    @Test
    @DisplayName("Delete client by id test")
    void deleteClientById() {
        doReturn(Optional.of(mockedClient)).when(clientRepository).findById(any(UUID.class));
        doNothing().when(clientRepository).delete(any(Client.class));

        clientService.deleteClientById(UUID.randomUUID());

        verify(clientRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).delete(any(Client.class));
    }

    @Test
    @DisplayName("Delete client by id throw exception test")
    @WithMockUser(roles = {"EMPLOYEE"})
    void deleteClientByIdThrowException() {
        doReturn(Optional.empty()).when(clientRepository).findById(any(UUID.class));

        assertThrows(ClientNotFoundException.class, () -> clientService.deleteClientById(UUID.randomUUID()));
    }
}
