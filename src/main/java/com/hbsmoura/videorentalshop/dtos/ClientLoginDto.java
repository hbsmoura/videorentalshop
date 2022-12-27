package com.hbsmoura.videorentalshop.dtos;

import com.hbsmoura.videorentalshop.model.Booking;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientLoginDto {

    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    private String password;

    private List<Booking> bookings;
}
