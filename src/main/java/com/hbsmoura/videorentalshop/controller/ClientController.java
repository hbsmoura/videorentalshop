package com.hbsmoura.videorentalshop.controller;

import com.hbsmoura.videorentalshop.dtos.ChangePasswordDto;
import com.hbsmoura.videorentalshop.dtos.ClientDto;
import com.hbsmoura.videorentalshop.dtos.ClientLoginDto;
import com.hbsmoura.videorentalshop.service.ClientService;
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
@RequestMapping("/clients")
@PreAuthorize("hasRole('EMPLOYEE')")
@Tag(name = "Client Controller")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAnonymous()")
    public ClientLoginDto createClient(@RequestBody @Valid ClientDto givenClient) {
        return clientService.createClient(givenClient);
    }

    @GetMapping
    public Page<ClientDto> listClients(Pageable pageable) {
        return clientService.listClients(pageable);
    }

    @GetMapping("/{id}")
    public ClientDto getClientById(@PathVariable UUID id) {
        return clientService.getClientById(id);
    }

    @GetMapping("/search/{text}")
    public Page<ClientDto> searchClientsByNameOrUsername(@PathVariable("text") String text, Pageable pageable) {
        return clientService.searchClientsByNameOrUsername(text, pageable);
    }

    @PutMapping
    @PreAuthorize("hasRole('CLIENT') and @authService.isItself(#givenClient.getId)")
    public ClientDto updateClient(@RequestBody @Valid ClientLoginDto givenClient) {
        return clientService.updateClient(givenClient);
    }

    @PatchMapping("/{id}/password")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Password successfully changed")
    @PreAuthorize("hasRole('CLIENT') and @authService.isItself(#id)")
    public void changePassword(@PathVariable UUID id, @RequestBody @Valid ChangePasswordDto changePasswordDto) {
        clientService.changePassword(id, changePasswordDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Client with given Id successfully deleted")
    @PreAuthorize("hasRole('MANAGER')")
    public void deleteClient(@PathVariable UUID id) {
        clientService.deleteClientById(id);
    }
}
