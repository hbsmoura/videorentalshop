package com.hbsmoura.videorentalshop.controller;

import com.hbsmoura.videorentalshop.config.apiresponse.*;
import com.hbsmoura.videorentalshop.dtos.BookingDto;
import com.hbsmoura.videorentalshop.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@Secured({"ROLE_EMPLOYEE"})
@Tag(name = "Booking Controller")
public class BookingController {

    private final BookingService bookingService;

    private class PageOfBookingDto extends PageImpl<BookingDto> {
        public PageOfBookingDto(List<BookingDto> content) {
            super(content);
        }
    }

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_CLIENT"})
    @Operation(
            summary = "Create booking",
            description = "Creates a new booking, saves on database and retrieves it"
    )
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    public BookingDto createBooking(@RequestBody @Valid BookingDto givenBooking) {
        return bookingService.createBooking(givenBooking);
    }

    @GetMapping
    @Operation(
            summary = "List bookings",
            description = "Retrieves a paged list of bookings"
    )
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    public Page<BookingDto> listBookings(Pageable pageable) {
        return bookingService.listBookings(pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get booking by id",
            description = "Retrieves a booking by its id"
    )
    @ApiResponseOk(content = @Content(schema = @Schema(implementation = BookingDto.class)))
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    public BookingDto getBookingById(@PathVariable UUID id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/search/state/{state}")
    @Operation(
            summary = "Search bookings by state",
            description = "Retrieves a list of bookings according to the given state parameter"
    )
    @ApiResponseOk(content = @Content(schema = @Schema(implementation = PageOfBookingDto.class)))
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    public Page<BookingDto> searchBookingsByState(@PathVariable String state, Pageable pageable) {
        return bookingService.searchBookingsByState(state, pageable);
    }

    @PutMapping
    @Secured({"ROLE_MANAGER"})
    @Operation(
            summary = "Update booking",
            description = "Updates the booking data"
    )
    @ApiResponseOk(content = @Content(schema = @Schema(implementation = BookingDto.class)))
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    public BookingDto updateBooking(@RequestBody @Valid BookingDto givenBooking) {
        return bookingService.updateBooking(givenBooking);
    }

    @PatchMapping("/cancel/{id}")
    @Secured({"ROLE_CLIENT"})
    @Operation(
            summary = "Cancel booking",
            description = "Cancels the booking"
    )
    @ApiResponseOk(content = @Content(schema = @Schema(implementation = BookingDto.class)))
    @ApiResponseNotFound
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    public BookingDto cancelBookingById(@PathVariable UUID id) {
        return bookingService.cancelBookingById(id);
    }

    @PatchMapping("/start")
    @Operation(
            summary = "Start rent",
            description = "Starts the rent of a movie"
    )
    @ApiResponseOk(content = @Content(schema = @Schema(implementation = BookingDto.class)))
    @ApiResponseNotFound
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    public BookingDto startRent(@RequestBody @Valid BookingDto givenBooking) {
        return bookingService.startRent(givenBooking);
    }

    @PatchMapping("/finalize")
    @Operation(
            summary = "Start rent",
            description = "Starts the rent of a movie"
    )
    @ApiResponseOk(content = @Content(schema = @Schema(implementation = BookingDto.class)))
    @ApiResponseNotFound
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    public BookingDto finalizeRent(@RequestBody @Valid BookingDto givenBooking) {
        return bookingService.finalizeRent(givenBooking);
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_MANAGER"})
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Booking with given Id successfully deleted")
    @Operation(
            summary = "Delete booking",
            description = "Deletes the booking from database"
    )
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    public void deleteBookingById(@PathVariable UUID id) {
        bookingService.deleteBookingById(id);
    }

}
