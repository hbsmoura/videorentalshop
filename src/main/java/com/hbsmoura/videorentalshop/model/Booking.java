package com.hbsmoura.videorentalshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hbsmoura.videorentalshop.enums.EnumBookingState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * The Booking class represents de model entity for a {@link com.hbsmoura.videorentalshop.model.Movie Movie} rent booking.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="movie_id", nullable=false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name="renter_id", nullable=false)
    @JsonBackReference
    private Client renter;

    private LocalDate rentStart;

    @ManyToOne
    @JoinColumn(name="rent_assignor_id")
    private Employee rentAssignor;

    private LocalDate estimatedDevolution;

    private LocalDate actualDevolution;

    @ManyToOne
    @JoinColumn(name="devolution_assignor_id")
    private Employee devolutionAssignor;

    private EnumBookingState state;

    private double regularPrice;

    private double penalty;

}
