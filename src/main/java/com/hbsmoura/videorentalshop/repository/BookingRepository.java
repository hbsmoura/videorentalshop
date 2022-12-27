package com.hbsmoura.videorentalshop.repository;

import com.hbsmoura.videorentalshop.enums.EnumBookingState;
import com.hbsmoura.videorentalshop.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    Page<Booking> findByState(EnumBookingState state, Pageable pageable);
}
