package com.hbsmoura.videorentalshop.dtos;

import com.hbsmoura.videorentalshop.config.hateoas.HateoasIdentity;
import com.hbsmoura.videorentalshop.config.hateoas.HateoasModel;
import com.hbsmoura.videorentalshop.controller.ClientController;
import com.hbsmoura.videorentalshop.model.Booking;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@HateoasModel(controller = ClientController.class)
public class ClientLoginDto extends RepresentationModel<ClientLoginDto> implements Serializable {
    private static final long serialVersionUID = 1L;

    @HateoasIdentity
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    private String password;

    private List<Booking> bookings;
}
