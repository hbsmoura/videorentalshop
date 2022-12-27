package com.hbsmoura.videorentalshop.controller;

import com.hbsmoura.videorentalshop.dtos.BookingDto;
import com.hbsmoura.videorentalshop.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestBody BookingDto givenBooking) {
        return bookingService.createBooking(givenBooking);
    }

    @GetMapping
    public Page<BookingDto> listBookings(Pageable pageable) {
        return bookingService.listBookings(pageable);
    }

    @GetMapping("/{id}")
    public BookingDto getBookingById(@PathVariable("id") UUID id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/search/state/{state}")
    public Page<BookingDto> searchBookingsByState(@PathVariable("state") String state, Pageable pageable) {
        return bookingService.searchBookingsByState(state, pageable);
    }

    @PutMapping
    public BookingDto updateBooking(@RequestBody BookingDto givenBooking) {
        return bookingService.updateBooking(givenBooking);
    }

    @PatchMapping("/cancel/{id}")
    public BookingDto cancelBookingById(@PathVariable("id") UUID id) {
        return bookingService.cancelBookingById(id);
    }

    @PatchMapping("/start")
    public BookingDto startRent(@RequestBody BookingDto givenBooking) {
        return bookingService.startRent(givenBooking);
    }

    @PatchMapping("/finalize")
    public BookingDto finalizeRent(@RequestBody BookingDto givenBooking) {
        return bookingService.finalizeRent(givenBooking);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Booking with given Id successfully deleted")
    public void deleteBookingById(@PathVariable UUID id) {
        bookingService.deleteBookingById(id);
    }

}
