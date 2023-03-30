package com.hbsmoura.videorentalshop.controller;

import com.hbsmoura.videorentalshop.dtos.BookingDto;
import com.hbsmoura.videorentalshop.service.BookingService;
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
@RequestMapping("/bookings")
@PreAuthorize("hasRole('EMPLOYEE')")
@Tag(name = "Booking Controller")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CLIENT')")
    public BookingDto createBooking(@RequestBody @Valid BookingDto givenBooking) {
        return bookingService.createBooking(givenBooking);
    }

    @GetMapping
    public Page<BookingDto> listBookings(Pageable pageable) {
        return bookingService.listBookings(pageable);
    }

    @GetMapping("/{id}")
    public BookingDto getBookingById(@PathVariable UUID id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/search/state/{state}")
    public Page<BookingDto> searchBookingsByState(@PathVariable String state, Pageable pageable) {
        return bookingService.searchBookingsByState(state, pageable);
    }

    @PutMapping
    @PreAuthorize("hasRole('MANAGER')")
    public BookingDto updateBooking(@RequestBody @Valid BookingDto givenBooking) {
        return bookingService.updateBooking(givenBooking);
    }

    @PatchMapping("/cancel/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public BookingDto cancelBookingById(@PathVariable UUID id) {
        return bookingService.cancelBookingById(id);
    }

    @PatchMapping("/start")
    public BookingDto startRent(@RequestBody @Valid BookingDto givenBooking) {
        return bookingService.startRent(givenBooking);
    }

    @PatchMapping("/finalize")
    public BookingDto finalizeRent(@RequestBody @Valid BookingDto givenBooking) {
        return bookingService.finalizeRent(givenBooking);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Booking with given Id successfully deleted")
    public void deleteBookingById(@PathVariable UUID id) {
        bookingService.deleteBookingById(id);
    }

}
