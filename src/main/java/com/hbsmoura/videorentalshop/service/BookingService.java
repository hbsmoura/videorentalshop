package com.hbsmoura.videorentalshop.service;

import com.hbsmoura.videorentalshop.dtos.BookingDto;
import com.hbsmoura.videorentalshop.enums.EnumBookingState;
import com.hbsmoura.videorentalshop.exceptions.*;
import com.hbsmoura.videorentalshop.model.Booking;
import com.hbsmoura.videorentalshop.model.Client;
import com.hbsmoura.videorentalshop.model.Employee;
import com.hbsmoura.videorentalshop.model.Movie;
import com.hbsmoura.videorentalshop.repository.BookingRepository;
import com.hbsmoura.videorentalshop.repository.ClientRepository;
import com.hbsmoura.videorentalshop.repository.EmployeeRepository;
import com.hbsmoura.videorentalshop.repository.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

import static com.hbsmoura.videorentalshop.exceptions.BookingCannotBeUpdatedFromRequestedException.Operation;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final MovieRepository movieRepository;
    private final ClientRepository clientRepository;

    private final EmployeeRepository employeeRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, MovieRepository movieRepository, ClientRepository clientRepository, EmployeeRepository employeeRepository) {
        this.bookingRepository = bookingRepository;
        this.movieRepository = movieRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Method for insertion of a new booking
     * @param givenBooking the booking to be created
     * @return the created booking
     * @throws MovieNotFoundException if there is no movie with the id of the given movie on the model layer
     * @throws ClientNotFoundException if there is no client with the id of the given renter on the model layer
     *
     */

    //only for clients
    public BookingDto createBooking(BookingDto givenBooking) {
        givenBooking.setId(null);

        Movie movie = movieRepository.findById(givenBooking.getMovie().getId()).orElseThrow(MovieNotFoundException::new);
        Client renter = clientRepository.findById(givenBooking.getRenter().getId()).orElseThrow(ClientNotFoundException::new);

        Booking booking = Booking.builder()
                .movie(movie)
                .renter(renter)
                .state(EnumBookingState.REQUESTED)
                .build();


        booking.setEstimatedDevolution(
                givenBooking.getEstimatedDevolution() != null ?
                        givenBooking.getEstimatedDevolution() :
                        LocalDate.now().plusDays(3)
        );

        Booking savedBooking = bookingRepository.save(booking);

        return new ModelMapper().map(savedBooking, BookingDto.class);
    }

    /**
     * Method for get a page of bookings with the given properties.
     * @param pageable the object that carries the page properties
     * @return a page of bookings from the model layer
     */

    public Page<BookingDto> listBookings(Pageable pageable) {
        Page<Booking> bookings = bookingRepository.findAll(pageable);
        return bookings.map(booking -> new ModelMapper().map(booking, BookingDto.class));
    }

    /**
     * Method for retrieve a booking by its id.
     * @param id the given id
     * @return the found movie
     * @throws BookingNotFoundException if there is no booking with the given id on the model layer
     */

    public BookingDto getBookingById(UUID id) {
        Booking booking =  bookingRepository.findById(id).orElseThrow(BookingNotFoundException::new);
        return new ModelMapper().map(booking, BookingDto.class);
    }

    /**
     * Method for search bookings by its state.
     * @param givenState the state for the search
     * @param pageable the object that carries the page properties
     * @throws NoSuchGenreException if there is no such state as the given
     * @return a page of found bookings from the model layer
     */

    public Page<BookingDto> searchBookingsByState(String givenState, Pageable pageable) {
        givenState = givenState.toUpperCase();
        try {
            EnumBookingState state = EnumBookingState.valueOf(givenState);
            Page<Booking> bookings = bookingRepository.findByState(state, pageable);
            return bookings.map(booking -> new ModelMapper().map(booking, BookingDto.class));
        } catch (IllegalArgumentException e) {
            throw new NoSuchGenreException(givenState);
        }
    }

    /**
     * Method for update a given booking.
     * @param givenBooking the booking to be updated
     * @return the updated booking
     * @throws BookingNotFoundException if there is no booking with the id of the given booking on the model layer
     * @throws MovieNotFoundException if there is no movie with the id of the given movie on the model layer
     * @throws ClientNotFoundException if there is no client with the id of the given renter on the model layer
     */

    // only for admins
    @Transactional
    public BookingDto updateBooking(BookingDto givenBooking) {
        Booking booking = bookingRepository.findById(givenBooking.getId()).orElseThrow(BookingNotFoundException::new);
        Movie movie = movieRepository.findById(givenBooking.getMovie().getId()).orElseThrow(MovieNotFoundException::new);
        Client renter = clientRepository.findById(givenBooking.getRenter().getId()).orElseThrow(ClientNotFoundException::new);

        booking.setMovie(movie);
        booking.setRenter(renter);
        booking.setRentStart(givenBooking.getRentStart());
        booking.setRentAssignor(givenBooking.getRentAssignor());
        booking.setEstimatedDevolution(givenBooking.getEstimatedDevolution());
        booking.setActualDevolution(givenBooking.getActualDevolution());
        booking.setDevolutionAssignor(givenBooking.getDevolutionAssignor());
        booking.setState(givenBooking.getState());
        booking.setRegularPrice(booking.getRegularPrice());
        booking.setPenalty(booking.getPenalty());

        Booking savedBooking = bookingRepository.save(booking);

        return new ModelMapper().map(savedBooking, BookingDto.class);
    }

    /**
     * Method for cancel a booking by a given id
     * @param id the given id
     * @return the updated booking
     * @throws BookingNotFoundException if there is no booking with the given id on the model layer
     * @throws BookingCannotBeUpdatedFromRequestedException if the state of the found booking is different the REQUESTED
     */

    // only for clients
    @Transactional
    public BookingDto cancelBookingById(UUID id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(BookingNotFoundException::new);

        if (booking.getState() != EnumBookingState.REQUESTED) throw new BookingCannotBeUpdatedFromRequestedException(Operation.CANCELED);

        booking.setState(EnumBookingState.CANCELED);
        booking.setPenalty(new BigDecimal(0).subtract(booking.getRegularPrice()));

        return new ModelMapper().map(booking, BookingDto.class);
    }

    /**
     * Method for initialize a booking rent from requested.
     * @param givenBooking the booking to be updated
     * @return the updated booking
     * @throws BookingNotFoundException if there is no booking with the id of the given booking on the model layer
     * @throws EmployeeNotFoundException if there is no employee (assignor) with the given id on the model layer
     * @throws BookingCannotBeUpdatedFromRequestedException if the booking state is different from requested
     * @throws MovieNotAvailableException if the movie is not available for rent
     */

    @Transactional
    public BookingDto startRent(BookingDto givenBooking) {
        Booking booking = bookingRepository.findById(givenBooking.getId()).orElseThrow(BookingNotFoundException::new);
        Employee assingor = employeeRepository.findById(givenBooking.getRentAssignor().getId()).orElseThrow(EmployeeNotFoundException::new);
        Movie movie = booking.getMovie();

        if (booking.getState() != EnumBookingState.REQUESTED)
            throw new BookingCannotBeUpdatedFromRequestedException(Operation.STARTED);
        if (movie.getQuantityAvailable() < 1)
            throw new MovieNotAvailableException();

        booking.setRentStart(LocalDate.now());
        booking.setRentAssignor(assingor);
        booking.setEstimatedDevolution(givenBooking.getEstimatedDevolution());
        booking.setState(EnumBookingState.RENTED);
        movie.setQuantityAvailable(movie.getQuantityAvailable()-1);
        booking.setRegularPrice(
                movie.getValuePerDay().multiply(
                        new BigDecimal(
                                Period
                                        .between(booking.getRentStart(), booking.getEstimatedDevolution())
                                        .getDays()
                        )
                )
        );

        movieRepository.save(movie);
        Booking savedBooking = bookingRepository.save(booking);

        return new ModelMapper().map(savedBooking, BookingDto.class);
    }

    /**
     * Method for finalize a booking rent from requested or rented states.
     * @param givenBooking the booking to be updated
     * @return the updated booking
     * @throws BookingNotFoundException if there is no booking with the id of the given booking on the model layer
     * @throws EmployeeNotFoundException if there is no employee (assignor) with the given id on the model layer
     * @throws BookingCannotBeFinalizedException if the booking state is different from requested or rented
     */

    @Transactional
    public BookingDto finalizeRent(BookingDto givenBooking) {
        Booking booking = bookingRepository.findById(givenBooking.getId()).orElseThrow(BookingNotFoundException::new);
        Employee assingor = employeeRepository.findById(givenBooking.getDevolutionAssignor().getId()).orElseThrow(EmployeeNotFoundException::new);
        Movie movie = booking.getMovie();

        if (booking.getState() != EnumBookingState.REQUESTED && booking.getState() != EnumBookingState.RENTED)
            throw new BookingCannotBeFinalizedException();

        booking.setActualDevolution(LocalDate.now());
        booking.setDevolutionAssignor(assingor);
        booking.setState(EnumBookingState.FINALIZED);

        if (booking.getState() == EnumBookingState.RENTED) {
            booking.setPenalty(
                    movie.getValuePerDay().multiply(
                            new BigDecimal(
                                    Period.between(booking.getRentStart(), booking.getActualDevolution()).getDays()
                            )
                    )
            );
            movie.setQuantityAvailable(movie.getQuantityAvailable()+1);
        } else {
            booking.setPenalty(new BigDecimal(0).subtract(booking.getRegularPrice()));
        }

        movieRepository.save(movie);
        Booking savedBooking = bookingRepository.save(booking);

        return new ModelMapper().map(savedBooking, BookingDto.class);
    }

    /**
     * Method for delete a booking by a given id.
     * @param id the given id
     * @throws BookingNotFoundException if there is no booking with the given id on the model layer
     */

    // only for admins
    public void deleteBookingById(UUID id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(BookingNotFoundException::new);
        bookingRepository.delete(booking);
    }
}
