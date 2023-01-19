package com.hbsmoura.videorentalshop.service;

import com.hbsmoura.videorentalshop.dtos.BookingDto;
import com.hbsmoura.videorentalshop.enums.EnumBookingState;
import com.hbsmoura.videorentalshop.enums.EnumMovieGenre;
import com.hbsmoura.videorentalshop.exceptions.*;
import com.hbsmoura.videorentalshop.model.Booking;
import com.hbsmoura.videorentalshop.model.Client;
import com.hbsmoura.videorentalshop.model.Employee;
import com.hbsmoura.videorentalshop.model.Movie;
import com.hbsmoura.videorentalshop.repository.BookingRepository;
import com.hbsmoura.videorentalshop.repository.ClientRepository;
import com.hbsmoura.videorentalshop.repository.EmployeeRepository;
import com.hbsmoura.videorentalshop.repository.MovieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    private Movie mockedMovie = Movie.builder()
            .id(UUID.randomUUID())
            .title("Movie Test")
            .direction("Director test")
            .duration(120)
            .year(1994)
            .info("Some info from the movie")
            .valuePerDay(new BigDecimal("1.2"))
            .totalQuantity(3)
            .quantityAvailable(2)
            .genres(EnumSet.of(EnumMovieGenre.ADVENTURE, EnumMovieGenre.ROMANCE, EnumMovieGenre.TEEN))
            .themes(new HashSet<>(Arrays.asList("Friendship", "Love", "Youth")))
            .build();
    private Employee mockedEmployee = Employee.builder()
            .id(UUID.randomUUID())
            .name("Employee Test")
            .username("employeetest")
            .password(UUID.randomUUID().toString())
            .manager(false)
            .build();

    private Client mockedClient = Client.builder()
            .id(UUID.randomUUID())
            .name("Client Test")
            .username("clienttest")
            .password(UUID.randomUUID().toString())
            .build();

    private Booking mockedBooking = Booking.builder()
            .id(UUID.randomUUID())
            .movie(mockedMovie)
            .renter(mockedClient)
            .estimatedDevolution(LocalDate.now().plusDays(3))
            .state(EnumBookingState.REQUESTED)
            .build();

    private Page<Booking> mockedPageBookings = new PageImpl<>(Collections.singletonList(mockedBooking));

    private BookingDto mockedBookingDto = new ModelMapper().map(mockedBooking, BookingDto.class);

    @Test
    @DisplayName("Create booking test")
    void createBookingTest() {
        doReturn(Optional.of(mockedMovie)).when(movieRepository).findById(any(UUID.class));
        doReturn(Optional.of(mockedClient)).when(clientRepository).findById(any(UUID.class));
        doReturn(mockedBooking).when(bookingRepository).save(any(Booking.class));

        BookingDto returnedBooking = bookingService.createBooking(mockedBookingDto);

        assertThat(returnedBooking.getId(), is(mockedBooking.getId()));
        assertThat(returnedBooking.getMovie(), is(mockedMovie));
        assertThat(returnedBooking.getRenter(), is(mockedClient));
        assertThat(returnedBooking.getRentStart(), is(nullValue()));
        assertThat(returnedBooking.getRentAssignor(), is(nullValue()));
        assertThat(returnedBooking.getEstimatedDevolution(), is(LocalDate.now().plusDays(3)));
        assertThat(returnedBooking.getActualDevolution(), is(nullValue()));
        assertThat(returnedBooking.getDevolutionAssignor(), is(nullValue()));
        assertThat(returnedBooking.getState(), is(EnumBookingState.REQUESTED));
        assertNull(returnedBooking.getRegularPrice());
        assertNull(returnedBooking.getPenalty());

    }

    @Test
    @DisplayName("Create booking without estimated devolution test")
    void createBookingWithoutEstimatedDevolutionTest() {
        mockedBooking.setEstimatedDevolution(null);
        doReturn(Optional.of(mockedMovie)).when(movieRepository).findById(any(UUID.class));
        doReturn(Optional.of(mockedClient)).when(clientRepository).findById(any(UUID.class));
        doReturn(mockedBooking).when(bookingRepository).save(any(Booking.class));

        BookingDto returnedBooking = bookingService.createBooking(new ModelMapper().map(mockedBooking, BookingDto.class));

        assertThat(returnedBooking.getId(), is(mockedBooking.getId()));
        assertThat(returnedBooking.getMovie(), is(mockedMovie));
        assertThat(returnedBooking.getRenter(), is(mockedClient));
        assertThat(returnedBooking.getRentStart(), is(nullValue()));
        assertThat(returnedBooking.getRentAssignor(), is(nullValue()));
        assertThat(returnedBooking.getEstimatedDevolution(), is(nullValue()));
        assertThat(returnedBooking.getActualDevolution(), is(nullValue()));
        assertThat(returnedBooking.getDevolutionAssignor(), is(nullValue()));
        assertThat(returnedBooking.getState(), is(EnumBookingState.REQUESTED));
        assertNull(returnedBooking.getRegularPrice());
        assertNull(returnedBooking.getPenalty());

    }

    @Test
    @DisplayName("Create booking throw MovieNotFoundException test")
    void createBookingThrowMovieNotFoundExceptionTest() {
        assertThrows(MovieNotFoundException.class, () -> bookingService.createBooking(mockedBookingDto));
    }

    @Test
    @DisplayName("Create booking throw ClientNotFoundException test")
    void createBookingThrowClientNotFoundExceptionTest() {
        doReturn(Optional.of(mockedMovie)).when(movieRepository).findById(any(UUID.class));

        assertThrows(ClientNotFoundException.class, () -> bookingService.createBooking(mockedBookingDto));
    }

    @Test
    @DisplayName("Paging list of bookings test")
    void listBookingsTest() {
        doReturn(mockedPageBookings).when(bookingRepository).findAll(any(Pageable.class));

        Page<BookingDto> returnedPage = bookingService.listBookings(mockedPageBookings.getPageable());

        assertThat(returnedPage.getContent().get(0), is(mockedBookingDto));
    }

    @Test
    @DisplayName("Get booking by the given id test")
    void getBookingByIdTest() {
        doReturn(Optional.of(mockedBooking)).when(bookingRepository).findById(any(UUID.class));

        BookingDto returnedBooking = bookingService.getBookingById(mockedBooking.getId());

        assertThat(returnedBooking.getId(), is(mockedBooking.getId()));
        assertThat(returnedBooking.getMovie(), is(mockedMovie));
        assertThat(returnedBooking.getRenter(), is(mockedClient));
        assertThat(returnedBooking.getRentStart(), is(mockedBooking.getRentStart()));
        assertThat(returnedBooking.getRentAssignor(), is(mockedBooking.getRentAssignor()));
        assertThat(returnedBooking.getEstimatedDevolution(), is(mockedBooking.getEstimatedDevolution()));
        assertThat(returnedBooking.getActualDevolution(), is(nullValue()));
        assertThat(returnedBooking.getDevolutionAssignor(), is(nullValue()));
        assertThat(returnedBooking.getState(), is(EnumBookingState.REQUESTED));
        assertThat(returnedBooking.getRegularPrice(), is(mockedBooking.getRegularPrice()));
        assertNull(returnedBooking.getPenalty());
    }

    @Test
    @DisplayName("Get booking by id throw BookingNotFoundException test")
    void getBookingByIdThrowBookingNotFoundExceptionTest() {
        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Paging list of bookings by state")
    void searchBookingsByStateTest() {
        doReturn(mockedPageBookings).when(bookingRepository)
                .findByState(any(EnumBookingState.class), any(Pageable.class));

        Page<BookingDto> returnedPage = bookingService.searchBookingsByState("Rented", mockedPageBookings.getPageable());

        assertThat(returnedPage.getContent().get(0), is(mockedBookingDto));

    }

    @Test
    @DisplayName("Paging list of bookings throw NoSuchGenreException test")
    void listBookingsByStateThrowNoSuchGenreExceptionTest() {
        assertThrows(NoSuchGenreException.class, () -> bookingService.searchBookingsByState("WrongState", Pageable.unpaged()));
    }

    @Test
    @DisplayName("Update booking test")
    void updateBookingTest() {
        doReturn(Optional.of(mockedBooking)).when(bookingRepository).findById(any(UUID.class));
        doReturn(Optional.of(mockedMovie)).when(movieRepository).findById(any(UUID.class));
        doReturn(Optional.of(mockedClient)).when(clientRepository).findById(any(UUID.class));
        doReturn(mockedBooking).when(bookingRepository).save(any(Booking.class));

        BookingDto returnedBooking = bookingService.updateBooking(mockedBookingDto);

        assertThat(returnedBooking.getId(), is(mockedBooking.getId()));
        assertThat(returnedBooking.getMovie(), is(mockedMovie));
        assertThat(returnedBooking.getRenter(), is(mockedClient));
        assertThat(returnedBooking.getRentStart(), is(mockedBooking.getRentStart()));
        assertThat(returnedBooking.getRentAssignor(), is(mockedBooking.getRentAssignor()));
        assertThat(returnedBooking.getEstimatedDevolution(), is(mockedBooking.getEstimatedDevolution()));
        assertThat(returnedBooking.getActualDevolution(), is(nullValue()));
        assertThat(returnedBooking.getDevolutionAssignor(), is(nullValue()));
        assertThat(returnedBooking.getState(), is(EnumBookingState.REQUESTED));
        assertThat(returnedBooking.getRegularPrice(), is(mockedBooking.getRegularPrice()));
        assertNull(returnedBooking.getPenalty());
    }

    @Test
    @DisplayName("Update booking throw BookingNotFoundException test")
    void updateBookingThrowBookingNotFoundExceptionTest() {
        assertThrows(BookingNotFoundException.class, () -> bookingService.updateBooking(mockedBookingDto));
    }

    @Test
    @DisplayName("Update booking throw MovieNotFoundException test")
    void updateBookingThrowMovieNotFoundExceptionTest() {
        doReturn(Optional.of(mockedBooking)).when(bookingRepository).findById(any(UUID.class));

        assertThrows(MovieNotFoundException.class, () -> bookingService.updateBooking(mockedBookingDto));
    }

    @Test
    @DisplayName("Update booking throw ClientNotFoundException test")
    void updateBookingThrowClientNotFoundExceptionTest() {
        doReturn(Optional.of(mockedBooking)).when(bookingRepository).findById(any(UUID.class));
        doReturn(Optional.of(mockedMovie)).when(movieRepository).findById(any(UUID.class));

        assertThrows(ClientNotFoundException.class, () -> bookingService.updateBooking(mockedBookingDto));
    }

    @Test
    @DisplayName("Cancel booking test")
    void cancelBookingTest() {
        mockedBooking.setRegularPrice(new BigDecimal("3.6"));

        doReturn(Optional.of(mockedBooking)).when(bookingRepository).findById(any(UUID.class));

        BookingDto returnedBooking = bookingService.cancelBookingById(mockedBooking.getId());

        assertThat(returnedBooking.getId(), is(mockedBooking.getId()));
        assertThat(returnedBooking.getMovie(), is(mockedMovie));
        assertThat(returnedBooking.getRenter(), is(mockedClient));
        assertThat(returnedBooking.getRentStart(), is(nullValue()));
        assertThat(returnedBooking.getRentAssignor(), is(nullValue()));
        assertThat(returnedBooking.getEstimatedDevolution(), is(mockedBooking.getEstimatedDevolution()));
        assertThat(returnedBooking.getActualDevolution(), is(nullValue()));
        assertThat(returnedBooking.getDevolutionAssignor(), is(nullValue()));
        assertThat(returnedBooking.getState(), is(EnumBookingState.CANCELED));
        assertThat(returnedBooking.getRegularPrice(), is(mockedBooking.getRegularPrice()));
        assertThat(returnedBooking.getPenalty(), is(mockedBooking.getPenalty()));
    }

    @Test
    @DisplayName("Cancel booking throw BookingNotFoundException test")
    void cancelBookingThrowBookingNotFoundExceptionTest() {
        assertThrows(
                BookingNotFoundException.class,
                () -> bookingService.cancelBookingById(UUID.randomUUID())
        );
    }

    @Test
    @DisplayName("Cancel booking throw BookingCannotBeUpdatedFromRequestedException test")
    void cancelBookingThrowBookingCannotBeUpdatedFromRequestedExceptionTest() {
        mockedBooking.setState(EnumBookingState.FINALIZED);
        doReturn(Optional.of(mockedBooking)).when(bookingRepository).findById(any(UUID.class));

        assertThrows(
                BookingCannotBeUpdatedFromRequestedException.class,
                () -> bookingService.cancelBookingById(UUID.randomUUID())
        );
    }

    @Test
    @DisplayName("Start rent test")
    void startRentTest() {
        mockedBooking.setRentAssignor(mockedEmployee);
        mockedBooking.setState(EnumBookingState.REQUESTED);

        doReturn(Optional.of(mockedBooking)).when(bookingRepository).findById(any(UUID.class));
        doReturn(Optional.of(mockedEmployee)).when(employeeRepository).findById(any(UUID.class));
        doReturn(mockedMovie).when(movieRepository).save(any(Movie.class));
        doReturn(mockedBooking).when(bookingRepository).save(any(Booking.class));

        BookingDto returnedBooking = bookingService.startRent(new ModelMapper().map(mockedBooking, BookingDto.class));

        assertThat(returnedBooking.getId(), is(mockedBooking.getId()));
        assertThat(returnedBooking.getMovie(), is(mockedMovie));
        assertThat(returnedBooking.getRenter(), is(mockedClient));
        assertThat(returnedBooking.getRentStart(), is(LocalDate.now()));
        assertThat(returnedBooking.getRentAssignor(), is(mockedEmployee));
        assertThat(returnedBooking.getEstimatedDevolution(), is(mockedBooking.getEstimatedDevolution()));
        assertThat(returnedBooking.getActualDevolution(), is(nullValue()));
        assertThat(returnedBooking.getDevolutionAssignor(), is(nullValue()));
        assertThat(returnedBooking.getState(), is(EnumBookingState.RENTED));
        assertThat(returnedBooking.getRegularPrice(), is(mockedBooking.getRegularPrice()));
        assertNull(returnedBooking.getPenalty());
    }

    @Test
    @DisplayName("Start rent throw BookingNotFoundException test")
    void startRentThrowBookingNotFoundExceptionTest() {
        assertThrows(BookingNotFoundException.class,
                () -> bookingService.startRent(mockedBookingDto));
    }

    @Test
    @DisplayName("Start rent throw EmployeeNotFoundException test")
    void startRentThrowEmployeeNotFoundExceptionTest() {
        mockedBooking.setRentAssignor(mockedEmployee);
        mockedBooking.setState(EnumBookingState.REQUESTED);

        doReturn(Optional.of(mockedBooking)).when(bookingRepository).findById(any(UUID.class));

        assertThrows(EmployeeNotFoundException.class,
                () -> bookingService.startRent(new ModelMapper().map(mockedBooking, BookingDto.class)));
    }

    @Test
    @DisplayName("Start rent throw BookingCannotBeUpdatedFromRequestedException test")
    void startRentThrowBookingCannotBeUpdatedFromRequestedExceptionTest() {
        mockedBooking.setRentAssignor(mockedEmployee);
        mockedBooking.setState(EnumBookingState.FINALIZED);

        doReturn(Optional.of(mockedBooking)).when(bookingRepository).findById(any(UUID.class));
        doReturn(Optional.of(mockedEmployee)).when(employeeRepository).findById(any(UUID.class));

        assertThrows(BookingCannotBeUpdatedFromRequestedException.class,
                () -> bookingService.startRent(new ModelMapper().map(mockedBooking, BookingDto.class)));
    }
    @Test
    @DisplayName("Start rent throw MovieNotAvailableException test")
    void startRentThrowMovieNotAvailableExceptionTest() {
        mockedBooking.setRentAssignor(mockedEmployee);
        mockedBooking.getMovie().setQuantityAvailable(0);

        doReturn(Optional.of(mockedBooking)).when(bookingRepository).findById(any(UUID.class));
        doReturn(Optional.of(mockedEmployee)).when(employeeRepository).findById(any(UUID.class));

        assertThrows(MovieNotAvailableException.class,
                () -> bookingService.startRent(new ModelMapper().map(mockedBooking, BookingDto.class)));
    }

    @Test
    @DisplayName("Finalize rent from requested test")
    void FinalizeRentFromRequestedTest() {
        mockedBooking.setRentStart(LocalDate.now().minusDays(1L));
        mockedBooking.setRentAssignor(mockedEmployee);
        mockedBooking.setRegularPrice(
                mockedMovie.getValuePerDay().multiply(
                        new BigDecimal(
                                Period.between(
                                        LocalDate.now().minusDays(1L),
                                        LocalDate.now()
                                ).getDays()
                        )
                )
        );
        mockedBooking.setDevolutionAssignor(mockedEmployee);

        BookingDto newDto = new ModelMapper().map(mockedBooking, BookingDto.class);

        doReturn(Optional.of(mockedBooking)).when(bookingRepository).findById(any(UUID.class));
        doReturn(Optional.of(mockedEmployee)).when(employeeRepository).findById(any(UUID.class));
        doReturn(mockedMovie).when(movieRepository).save(any(Movie.class));
        doReturn(mockedBooking).when(bookingRepository).save(any(Booking.class));

        BookingDto returnedBooking = bookingService.finalizeRent(newDto);

        assertThat(returnedBooking.getId(), is(mockedBooking.getId()));
        assertThat(returnedBooking.getMovie(), is(mockedMovie));
        assertThat(returnedBooking.getRenter(), is(mockedClient));
        assertThat(returnedBooking.getRentStart(), is(mockedBooking.getRentStart()));
        assertThat(returnedBooking.getRentAssignor(), is(mockedEmployee));
        assertThat(returnedBooking.getEstimatedDevolution(), is(mockedBooking.getEstimatedDevolution()));
        assertThat(returnedBooking.getActualDevolution(), is(mockedBooking.getActualDevolution()));
        assertThat(returnedBooking.getDevolutionAssignor(), is(mockedEmployee));
        assertThat(returnedBooking.getState(), is(EnumBookingState.FINALIZED));
        assertThat(returnedBooking.getRegularPrice(), is(mockedBooking.getRegularPrice()));
        assertThat(returnedBooking.getPenalty(), is(BigDecimal.ZERO.subtract(mockedBooking.getRegularPrice())));
        assertThat(
                returnedBooking.getRegularPrice()
                        .add(returnedBooking.getPenalty())
                        .compareTo(BigDecimal.ZERO),
                is(0));
    }

    @Test
    @DisplayName("Finalize rent from rented test")
    void FinalizeRentFromRentedTest() {
        mockedBooking.setRentStart(LocalDate.now().minusDays(1L));
        mockedBooking.setRentAssignor(mockedEmployee);
        mockedBooking.setRegularPrice(
                mockedMovie.getValuePerDay().multiply(
                        new BigDecimal(
                                Period.between(
                                        LocalDate.now().minusDays(1L),
                                        LocalDate.now()
                                ).getDays()
                        )
                )
        );
        mockedBooking.setDevolutionAssignor(mockedEmployee);
        mockedBooking.setState(EnumBookingState.RENTED);

        BookingDto newDto = new ModelMapper().map(mockedBooking, BookingDto.class);

        doReturn(Optional.of(mockedBooking)).when(bookingRepository).findById(any(UUID.class));
        doReturn(Optional.of(mockedEmployee)).when(employeeRepository).findById(any(UUID.class));
        doReturn(mockedMovie).when(movieRepository).save(any(Movie.class));
        doReturn(mockedBooking).when(bookingRepository).save(any(Booking.class));

        BookingDto returnedBooking = bookingService.finalizeRent(newDto);

        assertThat(returnedBooking.getId(), is(mockedBooking.getId()));
        assertThat(returnedBooking.getMovie(), is(mockedMovie));
        assertThat(returnedBooking.getRenter(), is(mockedClient));
        assertThat(returnedBooking.getRentStart(), is(mockedBooking.getRentStart()));
        assertThat(returnedBooking.getRentAssignor(), is(mockedEmployee));
        assertThat(returnedBooking.getEstimatedDevolution(), is(mockedBooking.getEstimatedDevolution()));
        assertThat(returnedBooking.getActualDevolution(), is(mockedBooking.getActualDevolution()));
        assertThat(returnedBooking.getDevolutionAssignor(), is(mockedEmployee));
        assertThat(returnedBooking.getState(), is(EnumBookingState.FINALIZED));
        assertThat(returnedBooking.getRegularPrice(), is(mockedBooking.getRegularPrice()));
        assertThat(returnedBooking.getPenalty(), is(BigDecimal.ZERO));
        assertThat(
                returnedBooking.getRegularPrice()
                        .add(returnedBooking.getPenalty())
                        .compareTo(mockedBooking.getRegularPrice()),
                is(0));
    }

    @Test
    @DisplayName("Finalize rent from rented with penalty test")
    void FinalizeRentFromRentedWithPenaltyTest() {
        mockedBooking.setRentStart(LocalDate.now().minusDays(5));
        mockedBooking.setRentAssignor(mockedEmployee);
        mockedBooking.setRegularPrice(
                mockedMovie.getValuePerDay().multiply(
                        new BigDecimal(
                                Period.between(
                                        LocalDate.now().minusDays(1),
                                        LocalDate.now()
                                ).getDays()
                        )
                )
        );
        mockedBooking.setDevolutionAssignor(mockedEmployee);
        mockedBooking.setEstimatedDevolution(LocalDate.now().minusDays(2));
        mockedBooking.setState(EnumBookingState.RENTED);

        BookingDto newDto = new ModelMapper().map(mockedBooking, BookingDto.class);

        doReturn(Optional.of(mockedBooking)).when(bookingRepository).findById(any(UUID.class));
        doReturn(Optional.of(mockedEmployee)).when(employeeRepository).findById(any(UUID.class));
        doReturn(mockedMovie).when(movieRepository).save(any(Movie.class));
        doReturn(mockedBooking).when(bookingRepository).save(any(Booking.class));

        BookingDto returnedBooking = bookingService.finalizeRent(newDto);

        assertThat(returnedBooking.getId(), is(mockedBooking.getId()));
        assertThat(returnedBooking.getMovie(), is(mockedMovie));
        assertThat(returnedBooking.getRenter(), is(mockedClient));
        assertThat(returnedBooking.getRentStart(), is(mockedBooking.getRentStart()));
        assertThat(returnedBooking.getRentAssignor(), is(mockedEmployee));
        assertThat(returnedBooking.getEstimatedDevolution(), is(mockedBooking.getEstimatedDevolution()));
        assertThat(returnedBooking.getActualDevolution(), is(mockedBooking.getActualDevolution()));
        assertThat(returnedBooking.getDevolutionAssignor(), is(mockedEmployee));
        assertThat(returnedBooking.getState(), is(EnumBookingState.FINALIZED));
        assertThat(returnedBooking.getRegularPrice(), is(mockedBooking.getRegularPrice()));
        assertThat(returnedBooking.getPenalty(), is(new BigDecimal("2.4")));
        assertThat(
                returnedBooking.getRegularPrice()
                        .add(returnedBooking.getPenalty())
                        .compareTo(new BigDecimal("3.6")),
                is(0));
    }

    @Test
    @DisplayName("Finalize rent throw BookingNotFoundException test")
    void finalizeRentBookingNotFoundExceptionTest() {
        assertThrows(BookingNotFoundException.class, () -> bookingService.finalizeRent(mockedBookingDto));
    }

    @Test
    @DisplayName("Finalize rent throw EmployeeNotFoundException test")
    void finalizeRentEmployeeNotFoundExceptionTest() {
        Booking newMockedBooking = Booking.builder()
                .id(mockedBooking.getId())
                .renter(mockedClient)
                .movie(mockedMovie)
                .estimatedDevolution(LocalDate.now().plusDays(3L))
                .state(EnumBookingState.RENTED)
                .rentAssignor(mockedEmployee)
                .rentStart(LocalDate.now().minusDays(1L))
                .regularPrice(
                        mockedMovie.getValuePerDay().multiply(
                                new BigDecimal(
                                        Period.between(
                                                LocalDate.now().minusDays(1L),
                                                LocalDate.now()
                                        ).getDays()
                                )
                        )
                )
                .devolutionAssignor(mockedEmployee)
                .actualDevolution(LocalDate.now())
                .build();

        BookingDto newDto = new ModelMapper().map(newMockedBooking, BookingDto.class);

        doReturn(Optional.of(newMockedBooking)).when(bookingRepository).findById(any(UUID.class));

        assertThrows(EmployeeNotFoundException.class, () -> bookingService.finalizeRent(newDto));
    }

    @Test
    @DisplayName("Finalize rent throw BookingCannotBeFinalizedException test")
    void finalizeRentBookingCannotBeFinalizedExceptionTest() {
        Booking newMockedBooking = Booking.builder()
                .id(mockedBooking.getId())
                .renter(mockedClient)
                .movie(mockedMovie)
                .estimatedDevolution(LocalDate.now().plusDays(3L))
                .state(EnumBookingState.FINALIZED)
                .rentAssignor(mockedEmployee)
                .rentStart(LocalDate.now().minusDays(1L))
                .regularPrice(
                        mockedMovie.getValuePerDay().multiply(
                                new BigDecimal(
                                        Period.between(
                                                LocalDate.now().minusDays(1L),
                                                LocalDate.now()
                                        ).getDays()
                                )
                        )
                )
                .devolutionAssignor(mockedEmployee)
                .actualDevolution(LocalDate.now())
                .build();

        BookingDto newDto = new ModelMapper().map(newMockedBooking, BookingDto.class);

        doReturn(Optional.of(newMockedBooking)).when(bookingRepository).findById(any(UUID.class));
        doReturn(Optional.of(mockedEmployee)).when(employeeRepository).findById(any(UUID.class));

        assertThrows(BookingCannotBeFinalizedException.class, () -> bookingService.finalizeRent(newDto));
    }

    @Test
    @DisplayName("Delete booking test")
    void deleteBookingTest() {
        doReturn(Optional.of(mockedBooking)).when(bookingRepository).findById(any(UUID.class));
        doNothing().when(bookingRepository).delete(any(Booking.class));

        bookingService.deleteBookingById(mockedBooking.getId());
    }

    @Test
    @DisplayName("Delete booking throw BookingNotFoundException test")
    void deleteBookingThrowBookingNotFoundExceptionTest() {
        assertThrows(BookingNotFoundException.class, () -> bookingService.deleteBookingById(UUID.randomUUID()));
    }
}
