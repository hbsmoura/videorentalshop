package com.hbsmoura.videorentalshop.dtos;

import com.hbsmoura.videorentalshop.config.hateoas.HateoasIdentity;
import com.hbsmoura.videorentalshop.config.hateoas.HateoasModel;
import com.hbsmoura.videorentalshop.controller.BookingController;
import com.hbsmoura.videorentalshop.enums.EnumBookingState;
import com.hbsmoura.videorentalshop.model.Client;
import com.hbsmoura.videorentalshop.model.Employee;
import com.hbsmoura.videorentalshop.model.Movie;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@HateoasModel(controller = BookingController.class)
public class BookingDto extends RepresentationModel<BookingDto> implements Serializable {
    private static final long serialVersionUID = 1L;

    @HateoasIdentity
    private UUID id;

    @NotNull
    private Movie movie;

    @NotNull
    private Client renter;

    private LocalDate rentStart;

    private Employee rentAssignor;

    @NotNull
    private LocalDate estimatedDevolution;

    private LocalDate actualDevolution;

    private Employee devolutionAssignor;

    private EnumBookingState state;

    private BigDecimal regularPrice;

    private BigDecimal penalty;
}
