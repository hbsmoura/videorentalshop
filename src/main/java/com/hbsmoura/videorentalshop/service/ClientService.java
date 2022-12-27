package com.hbsmoura.videorentalshop.service;

import com.hbsmoura.videorentalshop.dtos.ClientDto;
import com.hbsmoura.videorentalshop.dtos.ClientLoginDto;
import com.hbsmoura.videorentalshop.exceptions.ClientNotFoundException;
import com.hbsmoura.videorentalshop.model.Client;
import com.hbsmoura.videorentalshop.repository.ClientRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Service class for the {@link com.hbsmoura.videorentalshop.model.Client Client} model class.
 */

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final PasswordEncoder passwordEncoder;

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

        return clientToBeReturned;
    }

    /**
     * Method for get a page of clients with the given properties.
     * @param pageable the object that carries the page properties
     * @return a page of clients from the model layer
     */

    public Page<ClientDto> listClients(Pageable pageable) {
        Page<Client> clients = clientRepository.findAll(pageable);
        return clients.map(client -> new ModelMapper().map(client, ClientDto.class));
    }

    /**
     * Method for retrieve a client by it's id.
     * @param id the given id
     * @return the found client
     * @throws ClientNotFoundException if there is no client with the given id on the model layer
     */

    public ClientDto getClientById(UUID id) {
        Client client =  clientRepository.findById(id).orElseThrow(ClientNotFoundException::new);
        return new ModelMapper().map(client, ClientDto.class);
    }

    /**
     * Method for search clients by it's name or username.
     * @param text the text for the search
     * @param pageable the object that carries the page properties
     * @return a page of found clients from the model layer
     */

    public Page<ClientDto> searchClientsByNameOrUsername(String text, Pageable pageable) {
        Page<Client> clients = clientRepository.findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(text, text, pageable);
        return clients.map(client -> new ModelMapper().map(client, ClientDto.class));
    }

    /**
     * Method for update a given client.
     * @param givenClient the client to be updated
     * @return the updated client
     * @throws ClientNotFoundException if there is no client with the id of the given client on the model layer
     */

    public ClientLoginDto updateClient(ClientLoginDto givenClient) {
        Client client = clientRepository.findById(givenClient.getId()).orElseThrow(ClientNotFoundException::new);

        client.setName(givenClient.getName());
        client.setUsername(givenClient.getUsername());
        client.setPassword(givenClient.getPassword());
        client.setBookings(givenClient.getBookings());

        clientRepository.save(client);

        return new ModelMapper().map(client, ClientLoginDto.class);
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
