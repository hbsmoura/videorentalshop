package com.hbsmoura.videorentalshop.controller;

import com.hbsmoura.videorentalshop.dtos.ClientDto;
import com.hbsmoura.videorentalshop.dtos.ClientLoginDto;
import com.hbsmoura.videorentalshop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientLoginDto createClient(@RequestBody ClientDto givenClient) {
        return clientService.createClient(givenClient);
    }

    @GetMapping
    public Page<ClientDto> listClients(Pageable pageable) {
        return clientService.listClients(pageable);
    }

    @GetMapping("/{id}")
    public ClientDto getClientById(@PathVariable("id") UUID id) {
        return clientService.getClientById(id);
    }

    @GetMapping("/search/{text}")
    public Page<ClientDto> searchClientsByNameOrUsername(@PathVariable("text") String text, Pageable pageable) {
        return clientService.searchClientsByNameOrUsername(text, pageable);
    }

    @PutMapping
    public ClientLoginDto updateClient(@RequestBody ClientLoginDto givenClient) {
        return clientService.updateClient(givenClient);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Client with given Id successfully deleted")
    public void deleteClient(@PathVariable("id") UUID id) {
        clientService.deleteClientById(id);
    }
}
