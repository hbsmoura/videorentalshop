package com.hbsmoura.videorentalshop.controller;

import com.hbsmoura.videorentalshop.config.apiresponse.ApiResponseForbidden;
import com.hbsmoura.videorentalshop.config.apiresponse.ApiResponseNotFound;
import com.hbsmoura.videorentalshop.config.apiresponse.ApiResponseOk;
import com.hbsmoura.videorentalshop.config.apiresponse.ApiResponseUnauthorized;
import com.hbsmoura.videorentalshop.config.hateoas.HateoasLink;
import com.hbsmoura.videorentalshop.dtos.ChangePasswordDto;
import com.hbsmoura.videorentalshop.dtos.ClientDto;
import com.hbsmoura.videorentalshop.dtos.ClientLoginDto;
import com.hbsmoura.videorentalshop.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/clients")
@Secured({"ROLE_EMPLOYEE"})
@Tag(name = "Client Controller")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ANONYMOUS"})
    @Operation(
            summary = "Create client",
            description = "Creates a new client, saves on database and retrieves it"
    )
    @ApiResponseForbidden
    @HateoasLink(relation = "Create", requestType = "POST")
    public ClientLoginDto createClient(@RequestBody @Valid ClientDto givenClient) {
        return clientService.createClient(givenClient);
    }

    @GetMapping
    @Operation(
            summary = "List clients",
            description = "Retrieves a paged list of clients"
    )
    @ApiResponseUnauthorized @ApiResponseForbidden
    @HateoasLink(relation = "List")
    public Page<ClientDto> listClients(Pageable pageable) {
        return clientService.listClients(pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get client by id",
            description = "Retrieves a client by its id"
    )
    @ApiResponseOk(content = @Content(schema = @Schema(implementation = ClientDto.class)))
    @ApiResponseNotFound @ApiResponseUnauthorized @ApiResponseForbidden
    @HateoasLink(selfRel = true)
    public ClientDto getClientById(@PathVariable UUID id) {
        return clientService.getClientById(id);
    }

    @GetMapping("/search/{text}")
    @Operation(
            summary = "Search clients by name or username",
            description = "Retrieves a list of clients according to the given parameter text"
    )
    @ApiResponseUnauthorized@ApiResponseForbidden
    @HateoasLink(relation = "Search by name or username")
    public Page<ClientDto> searchClientsByNameOrUsername(@PathVariable("text") String text, Pageable pageable) {
        return clientService.searchClientsByNameOrUsername(text, pageable);
    }

    @PutMapping
    @PreAuthorize("hasRole('CLIENT') and @authService.isItself(#givenClient.getId)")
    @Operation(
            summary = "Update client",
            description = "Updates the client data"
    )
    @ApiResponseOk(content = @Content(schema = @Schema(implementation = ClientDto.class)))
    @ApiResponseNotFound @ApiResponseUnauthorized @ApiResponseForbidden
    @HateoasLink(relation = "Update", requestType = "PUT")
    public ClientDto updateClient(@RequestBody @Valid ClientLoginDto givenClient) {
        return clientService.updateClient(givenClient);
    }

    @PatchMapping("/{id}/password")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Password successfully changed")
    @PreAuthorize("hasRole('CLIENT') and @authService.isItself(#id)")
    @Operation(
            summary = "Change password",
            description = "Updates the clients password"
    )
    @ApiResponseNotFound @ApiResponseUnauthorized @ApiResponseForbidden
    @HateoasLink(relation = "Change password", requestType = "PATCH")
    public void changePassword(@PathVariable UUID id, @RequestBody @Valid ChangePasswordDto changePasswordDto) {
        clientService.changePassword(id, changePasswordDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Client with given Id successfully deleted")
    @Secured({"ROLE_MANAGER"})
    @Operation(
            summary = "Delete client",
            description = "Deletes a client from database"
    )
    @ApiResponseNotFound @ApiResponseUnauthorized @ApiResponseForbidden
    @HateoasLink(relation = "Delete", requestType = "DELETE")
    public void deleteClient(@PathVariable UUID id) {
        clientService.deleteClientById(id);
    }
}
