package com.hbsmoura.videorentalshop.config.initialtestconfig;

import com.hbsmoura.videorentalshop.dtos.*;
import com.hbsmoura.videorentalshop.model.Client;
import com.hbsmoura.videorentalshop.model.Movie;
import com.hbsmoura.videorentalshop.service.BookingService;
import com.hbsmoura.videorentalshop.service.ClientService;
import com.hbsmoura.videorentalshop.service.EmployeeService;
import com.hbsmoura.videorentalshop.service.MovieService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

import static com.hbsmoura.videorentalshop.enums.EnumMovieGenre.*;

@Component
public class DataTest {
    private final ClientService clientService;
    private final MovieService movieService;
    private final EmployeeService employeeService;
    private final BookingService bookingService;

    @Autowired
    public DataTest(ClientService clientService, MovieService movieService, EmployeeService employeeService, BookingService bookingService) {
        this.clientService = clientService;
        this.movieService = movieService;
        this.employeeService = employeeService;
        this.bookingService = bookingService;
    }

    @Bean
    void populate() {
        ClientLoginDto client1 = clientService.createClient(
                ClientDto.builder()
                        .name("Karen Peris")
                        .username("karenperis")
                        .build()
        );

        ClientLoginDto client2 = clientService.createClient(
                ClientDto.builder()
                        .name("Don Peris")
                        .username("donperis")
                        .build()
        );

        ClientLoginDto client3 = clientService.createClient(
                ClientDto.builder()
                        .name("Mike Bitts")
                        .username("mikebitts")
                        .build()
        );

        EmployeeLoginDto employee1 = employeeService.createEmployee(
                EmployeeDto.builder()
                        .name("Elizabeth Fraser")
                        .username("bethfraser")
                        .manager(false)
                        .build()
        );

        EmployeeLoginDto employee2 = employeeService.createEmployee(
                EmployeeDto.builder()
                        .name("Robin Guthrie")
                        .username("robinguthrie")
                        .manager(false)
                        .build()
        );

        EmployeeLoginDto employee3 = employeeService.createEmployee(
                EmployeeDto.builder()
                        .name("Will Heggie")
                        .username("willheggie")
                        .manager(false)
                        .build()
        );

        EmployeeLoginDto employee4 = employeeService.createEmployee(
                EmployeeDto.builder()
                        .name("Simon Raymonde")
                        .username("simonraymonde")
                        .manager(true)
                        .build()
        );

        List<ClientLoginDto> clientList = Arrays.asList(client1, client2, client3);
        List<EmployeeLoginDto> employeeList = Arrays.asList(employee1, employee2, employee3, employee4);

        System.out.println();
        System.out.println("***** Test users *****");
        clientList.forEach(System.out::println);
        employeeList.forEach(System.out::println);
        System.out.println("******************************");
        System.out.println();

        MovieDto movie1 = movieService.createMovie(
                MovieDto.builder()
                        .title("Pulp Fiction")
                        .direction("Quentin Tarantino")
                        .duration(154)
                        .year(1994)
                        .info("The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.")
                        .totalQuantity(3)
                        .valuePerDay(new BigDecimal("1.2"))
                        .genres(EnumSet.of(ACTION, CRIME, DRAMA))
                        .themes(new HashSet<>(Arrays.asList("Bang bang", "Mafia", "Morality", "Revenge", "Violence")))
                        .build()
        );

        MovieDto movie2 = movieService.createMovie(
                MovieDto.builder()
                        .title("The Shining")
                        .direction("Stanley Kubrick")
                        .duration(146)
                        .year(1980)
                        .info("Jack Torrance becomes winter caretaker at the isolated Overlook Hotel in Colorado, hoping to cure his writer's block. He settles in along with his wife, Wendy, and his son, Danny, who is plagued by psychic premonitions")
                        .totalQuantity(3)
                        .valuePerDay(new BigDecimal("1.3"))
                        .genres(EnumSet.of(THRILLER, HORROR, MYSTERY))
                        .themes(new HashSet<>(Arrays.asList(
                                "Fear", "Insanity", "Madness", "Murder", "Paranormal Activity", "Revenge", "Violence"
                        )))
                        .build()
        );

        MovieDto movie3 = movieService.createMovie(
                MovieDto.builder()
                        .title("Django Unchained")
                        .direction("Quentin Tarantino")
                        .duration(165)
                        .year(2012)
                        .info("With the help of a German bounty-hunter, a freed slave sets out to rescue his wife from a brutal plantation-owner in Mississippi")
                        .totalQuantity(4)
                        .valuePerDay(new BigDecimal("1.1"))
                        .genres(EnumSet.of(ACTION, DRAMA, HISTORICAL, WESTERN))
                        .themes(new HashSet<>(Arrays.asList("Bang bang", "Morality", "Power", "Racism", "Revenge", "Slavery", "Violence")))
                        .build()
        );

        bookingService.createBooking(
                BookingDto.builder()
                        .movie(new ModelMapper().map(movie1, Movie.class))
                        .renter(new ModelMapper().map(client1, Client.class))
                        .estimatedDevolution(LocalDate.of(2022, 12, 22))
                        .build()
        );
    }
}
