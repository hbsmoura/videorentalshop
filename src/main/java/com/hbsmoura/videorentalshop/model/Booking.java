package com.hbsmoura.videorentalshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hbsmoura.videorentalshop.enums.EnumBookingState;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * The Booking class represents de model entity for a {@link com.hbsmoura.videorentalshop.model.Movie Movie} rent booking.
 */

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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

    private BigDecimal regularPrice;

    private BigDecimal penalty;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Booking booking = (Booking) o;
        return id != null && Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
