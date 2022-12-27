package com.hbsmoura.videorentalshop.dtos;

import com.hbsmoura.videorentalshop.model.Booking;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto extends RepresentationModel<ClientDto> {

    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    private List<Booking> bookings;
}
