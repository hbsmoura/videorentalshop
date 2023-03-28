package com.hbsmoura.videorentalshop.service;

import com.hbsmoura.videorentalshop.controller.ClientController;
import com.hbsmoura.videorentalshop.dtos.ChangePasswordDto;
import com.hbsmoura.videorentalshop.dtos.ClientDto;
import com.hbsmoura.videorentalshop.dtos.ClientLoginDto;
import com.hbsmoura.videorentalshop.exceptions.ClientNotFoundException;
import com.hbsmoura.videorentalshop.exceptions.PasswordNotMachException;
import com.hbsmoura.videorentalshop.model.Client;
import com.hbsmoura.videorentalshop.repository.ClientRepository;
import com.hbsmoura.videorentalshop.utils.LinkReferrer;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Service class for the {@link com.hbsmoura.videorentalshop.model.Client Client} model class.
 */

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final PasswordEncoder passwordEncoder;

    private static final String METHOD_NAME = "getClientById";

    @Autowired
    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Method for insertion of a new client.
     * Attaches a random alphanumeric password for the given client
     * @param givenClient the client to be created
     * @return the created client with attached random password
     */

    public ClientLoginDto createClient(ClientDto givenClient) {
        String randomPass = RandomStringUtils.randomAlphanumeric(12);
        Client newClient = Client.builder()
                .name(givenClient.getName())
                .username(givenClient.getUsername())
                .password(passwordEncoder.encode(randomPass))
                .bookings(new ArrayList<>())
                .build();

        Client savedClient = clientRepository.save(newClient);

        ClientLoginDto clientToBeReturned = new ModelMapper().map(savedClient, ClientLoginDto.class);
        clientToBeReturned.setPassword(randomPass);

        return LinkReferrer.doRefer(clientToBeReturned, clientToBeReturned.getId(), ClientController.class);
    }

    /**
     * Method for get a page of clients with the given properties.
     * @param pageable the object that carries the page properties
     * @return a page of clients from the model layer
     */

    public Page<ClientDto> listClients(Pageable pageable) {
        Page<Client> clients = clientRepository.findAll(pageable);
        Page<ClientDto> clientsDto = clients.map(client -> new ModelMapper().map(client, ClientDto.class));

        Method method = LinkReferrer.extractMethod(ClientController.class, METHOD_NAME, UUID.class);

        return clientsDto.map(m -> LinkReferrer.doRefer(m, m.getId(), ClientController.class, method));
    }

    /**
     * Method for retrieve a client by its id.
     * @param id the given id
     * @return the found client
     * @throws ClientNotFoundException if there is no client with the given id on the model layer
     */

    public ClientDto getClientById(UUID id) {
        Client client =  clientRepository.findById(id).orElseThrow(ClientNotFoundException::new);
        ClientDto clientDto = new ModelMapper().map(client, ClientDto.class);

        return LinkReferrer.doRefer(clientDto, clientDto.getId(), ClientController.class);
    }

    /**
     * Method for search clients by its name or username.
     * @param text the text for the search
     * @param pageable the object that carries the page properties
     * @return a page of found clients from the model layer
     */

    public Page<ClientDto> searchClientsByNameOrUsername(String text, Pageable pageable) {
        Page<Client> clients = clientRepository.findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(text, text, pageable);
        Page<ClientDto> clientsDto = clients.map(client -> new ModelMapper().map(client, ClientDto.class));

        Method method = LinkReferrer.extractMethod(ClientController.class, METHOD_NAME, UUID.class);

        return clientsDto.map(m -> LinkReferrer.doRefer(m, m.getId(), ClientController.class, method));
    }

    /**
     * Method for update a given client.
     * @param givenClient the client to be updated
     * @return the updated client
     * @throws ClientNotFoundException if there is no client with the id of the given client on the model layer
     */

    public ClientDto updateClient(ClientLoginDto givenClient) {
        Client client = clientRepository.findById(givenClient.getId()).orElseThrow(ClientNotFoundException::new);

        client.setName(givenClient.getName());
        client.setUsername(givenClient.getUsername());

        clientRepository.save(client);

        ClientDto clientDto = new ModelMapper().map(client, ClientDto.class);

        return LinkReferrer.doRefer(clientDto, clientDto.getId(), ClientController.class);
    }

    /**
     * Method for change the password of a client.
     * @param id the given id
     * @param changePasswordDto object containing the client's id, the current password and the new one
     * @throws ClientNotFoundException if there is no client with the given id on the model layer
     * @throws PasswordNotMachException if the given current password does not match with the saved one
     */


    public void changePassword(UUID id, ChangePasswordDto changePasswordDto) {
        Client client = clientRepository.findById(id).orElseThrow(ClientNotFoundException::new);

        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), client.getPassword()))
            throw new PasswordNotMachException();

        client.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));

        clientRepository.save(client);
    }

    /**
     * Method for delete a client by a given id.
     * @param id the given id
     * @throws ClientNotFoundException if there is no client with the given id on the model layer
     */

    public void deleteClientById(UUID id) {
        Client client = clientRepository.findById(id).orElseThrow(ClientNotFoundException::new);
        clientRepository.delete(client);
    }
}
