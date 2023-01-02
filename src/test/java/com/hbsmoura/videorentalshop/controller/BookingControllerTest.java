package com.hbsmoura.videorentalshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hbsmoura.videorentalshop.dtos.BookingDto;
import com.hbsmoura.videorentalshop.enums.EnumBookingState;
import com.hbsmoura.videorentalshop.enums.EnumMovieGenre;
import com.hbsmoura.videorentalshop.model.Booking;
import com.hbsmoura.videorentalshop.model.Client;
import com.hbsmoura.videorentalshop.model.Employee;
import com.hbsmoura.videorentalshop.model.Movie;
import com.hbsmoura.videorentalshop.service.BookingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    private final Movie mockedMovie = Movie.builder()
            .id(UUID.randomUUID())
            .title("Movie Test")
            .direction("Director test")
            .duration(120)
            .year(1994)
            .info("Some info from the movie")
            .valuePerDay(new BigDecimal(1d))
            .totalQuantity(3)
            .quantityAvailable(2)
            .genres(EnumSet.of(EnumMovieGenre.ADVENTURE, EnumMovieGenre.ROMANCE, EnumMovieGenre.TEEN))
            .themes(new HashSet<>(Arrays.asList("Friendship", "Love", "Youth")))
            .build();
    private final Employee mockedEmployee = Employee.builder()
            .id(UUID.randomUUID())
            .name("Employee Test")
            .username("employeetest")
            .password(UUID.randomUUID().toString())
            .manager(false)
            .build();

    private final Client mockedClient = Client.builder()
            .id(UUID.randomUUID())
            .name("Client Test")
            .username("clienttest")
            .password(UUID.randomUUID().toString())
            .build();

    private final Booking mockedBooking = Booking.builder()
            .id(UUID.randomUUID())
            .renter(mockedClient)
            .movie(mockedMovie)
            .estimatedDevolution(LocalDate.now().plusDays(3L))
            .state(EnumBookingState.RENTED)
            .rentAssignor(mockedEmployee)
            .rentStart(LocalDate.now())
            .regularPrice(
                    mockedMovie.getValuePerDay().multiply(
                            new BigDecimal(Period.between(
                                    LocalDate.now(), LocalDate.now().plusDays(3L)
                                ).getDays())
                    )
            )
            .build();

    private final BookingDto mockedBookingDto = new ModelMapper().map(mockedBooking, BookingDto.class);

    @Test
    @DisplayName("Create booking test")
    @WithMockUser(roles = "CLIENT")
    void createBookingTest() throws Exception {
        doReturn(mockedBookingDto).when(bookingService).createBooking(any(BookingDto.class));

        mockMvc
                .perform(
                        post("/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(BookingDto.builder().build()))

                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("List bookings test")
    @WithMockUser(roles = "EMPLOYEE")
    void listBookingsTest() throws Exception {
        Page<BookingDto> page = new PageImpl<>(Collections.singletonList(mockedBookingDto));

        doReturn(page).when(bookingService).listBookings(any(Pageable.class));

        mockMvc
                .perform(
                        get("/bookings")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(page)));
    }

    @Test
    @DisplayName("Get booking by id test")
    @WithMockUser(roles = "EMPLOYEE")
    void getBookingByIdTest() throws Exception {
        doReturn(mockedBookingDto).when(bookingService).getBookingById(any(UUID.class));

        mockMvc
                .perform(
                        get("/bookings/" + UUID.randomUUID())
                )
                .andExpect(status().isOk())
                .andExpect(content().json(getJsonStringOfBookingDtoWithoutLink()));
    }

    @Test
    @DisplayName("Search bookings by state test")
    @WithMockUser(roles = "EMPLOYEE")
    void searchBookingsByStateTest() throws Exception {
        Page<BookingDto> page = new PageImpl<>(Collections.singletonList(mockedBookingDto));

        doReturn(page).when(bookingService)
                .searchBookingsByState(anyString(), any(Pageable.class));

        mockMvc
                .perform(
                        get("/bookings/search/state/rented")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(page)));
    }

    @Test
    @DisplayName("Update booking test")
    @WithMockUser(roles = "MANAGER")
    void updateBookingTest() throws Exception {
        doReturn(mockedBookingDto).when(bookingService).updateBooking(any(BookingDto.class));

        mockMvc
                .perform(
                        put("/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(BookingDto.builder().build()))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Cancel booking by id test")
    @WithMockUser(roles = "EMPLOYEE")
    void cancelBookingByIdTest() throws Exception {
        BookingDto newBookingDto = BookingDto.builder()
                .id(mockedBooking.getId())
                .renter(mockedClient)
                .movie(mockedMovie)
                .estimatedDevolution(mockedBooking.getEstimatedDevolution())
                .state(EnumBookingState.CANCELED)
                .rentAssignor(mockedEmployee)
                .rentStart(LocalDate.now())
                .regularPrice(mockedBooking.getRegularPrice())
                .build();

        ObjectNode objectNode = mapper.readTree(mapper.writeValueAsString(newBookingDto)).deepCopy();
        String jsonObject = objectNode.without("links").toPrettyString();

        doReturn(newBookingDto).when(bookingService).cancelBookingById(any(UUID.class));

        mockMvc
                .perform(
                        patch("/bookings/cancel/" + newBookingDto.getId())
                )
                .andExpect(status().isOk())
                .andExpect(content().json(jsonObject));
    }

    @Test
    @DisplayName("Start rent test")
    @WithMockUser(roles = "EMPLOYEE")
    void startRentTest() throws Exception {
        doReturn(mockedBookingDto).when(bookingService).startRent(any(BookingDto.class));

        mockMvc
                .perform(
                        patch("/bookings/start")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(BookingDto.builder().build()))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Finalize rent test")
    @WithMockUser(roles = "EMPLOYEE")
    void finalizeRentTest() throws Exception {
        BookingDto newBookingDto = BookingDto.builder()
                .id(mockedBooking.getId())
                .renter(mockedClient)
                .movie(mockedMovie)
                .estimatedDevolution(mockedBooking.getEstimatedDevolution())
                .state(EnumBookingState.FINALIZED)
                .rentAssignor(mockedEmployee)
                .rentStart(LocalDate.now())
                .regularPrice(mockedBooking.getRegularPrice())
                .build();

        ObjectNode objectNode = mapper.readTree(mapper.writeValueAsString(newBookingDto)).deepCopy();
        String jsonObject = objectNode.without("links").toString();

        doReturn(newBookingDto).when(bookingService).finalizeRent(any(BookingDto.class));

        mockMvc
                .perform(
                        patch("/bookings/finalize")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(BookingDto.builder().build()))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(jsonObject));
    }

    @Test
    @DisplayName("Delete movie test")
    @WithMockUser(roles = "MANAGER")
    void deleteMovieTest() throws Exception {
        doNothing().when(bookingService).deleteBookingById(any(UUID.class));

        mockMvc
                .perform(
                        delete("/bookings/" + mockedBookingDto.getId())
                )
                .andExpect(status().isNoContent())
                .andExpect(status().reason("Booking with given Id successfully deleted"));
    }

    // The Employee DTO object instance without the property "links"
    // to prevent mismatch, due to Hateoas support
    private String getJsonStringOfBookingDtoWithoutLink() throws JsonProcessingException {
        ObjectNode objectNode = mapper.readTree(mapper.writeValueAsString(mockedBooking)).deepCopy();
        return objectNode.without("links").toString();
    }
}
