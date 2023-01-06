package com.hbsmoura.videorentalshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hbsmoura.videorentalshop.config.security.SecurityConfigTest;
import com.hbsmoura.videorentalshop.dtos.MovieDto;
import com.hbsmoura.videorentalshop.enums.EnumMovieGenre;
import com.hbsmoura.videorentalshop.model.Movie;
import com.hbsmoura.videorentalshop.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = MovieController.class)
@Import(SecurityConfigTest.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private MovieService movieService;

    private Movie mockedMovie;
    private MovieDto mockedMovieDto;

    @BeforeEach
    public void setup() {
         mockedMovie = Movie.builder()
                .id(UUID.randomUUID())
                .title("Mocked Movie")
                .direction("Mocked Director")
                .year(2000)
                .info("Mocked info")
                .totalQuantity(3)
                .quantityAvailable(2)
                .valuePerDay(new BigDecimal(1.2))
                .genres(EnumSet.of(EnumMovieGenre.ACTION, EnumMovieGenre.ADVENTURE, EnumMovieGenre.FANTASY))
                .themes(Set.of("Cars", "Running", "Friendship", "Loyalty"))
                .build();

        mockedMovieDto = new ModelMapper().map(mockedMovie, MovieDto.class);
    }

    @Test
    @DisplayName("Create movie test")
    @WithMockUser(roles = {"EMPLOYEE"})
    void createMovieTest() throws Exception {
        MovieDto movieDto = new ModelMapper().map(mockedMovie, MovieDto.class);

        doReturn(movieDto).when(movieService).createMovie(any(MovieDto.class));

        mockMvc
                .perform(
                        post("/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(mockedMovieDto))
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(getJsonStringOfMovieDtoWithoutLink()));
    }

    @Test
    @DisplayName("List movies test")
    void listMoviesTest() throws Exception {
        Page<MovieDto> page = new PageImpl<>(Collections.singletonList(mockedMovieDto));

        doReturn(page).when(movieService).listMovies(any(Pageable.class));

        mockMvc
                .perform(
                        get("/movies")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(page)));
    }

    @Test
    @DisplayName("Get movie by id test")
    void getMovieByIdTest() throws Exception {

        doReturn(mockedMovieDto).when(movieService).getMovieById(any(UUID.class));

        mockMvc
                .perform(
                        get("/movies/" + UUID.randomUUID())
                )
                .andExpect(status().isOk())
                .andExpect(content().json(getJsonStringOfMovieDtoWithoutLink()));
    }

    @Test
    @DisplayName("Search movies by title, direction or info test")
    void searchMoviesByTitleDirectionOrInfoTest() throws Exception {
        Page<MovieDto> page = new PageImpl<>(Collections.singletonList(mockedMovieDto));

        doReturn(page).when(movieService)
                .searchMoviesByTitleOrDirectionOrInfo(anyString(), any(Pageable.class));

        mockMvc
                .perform(
                        get("/movies/search/sometext")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(page)));
    }

    @Test
    @DisplayName("Search movies by genre test")
    void searchMoviesByGenreTest() throws Exception {
        Page<MovieDto> page = new PageImpl<>(Collections.singletonList(mockedMovieDto));

        doReturn(page).when(movieService)
                .searchMoviesByGenre(anyString(), any(Pageable.class));

        mockMvc
                .perform(
                        get("/movies/search/genre/action")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(page)));
    }

    @Test
    @DisplayName("Search movies by theme test")
    void searchMoviesByThemeTest() throws Exception {
        Page<MovieDto> page = new PageImpl<>(Collections.singletonList(mockedMovieDto));

        doReturn(page).when(movieService)
                .searchMoviesByTheme(anyString(), any(Pageable.class));

        mockMvc
                .perform(
                        get("/movies/search/theme/friendship")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(page)));
    }

    @Test
    @DisplayName("Update movie test")
    @WithMockUser(roles = {"EMPLOYEE"})
    void updateMovieTest() throws Exception {
        MovieDto mockedMovieDto = new ModelMapper().map(mockedMovie, MovieDto.class);

        doReturn(mockedMovieDto).when(movieService).updateMovie(any(MovieDto.class));

        mockMvc
                .perform(
                        put("/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(mockedMovieDto))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(getJsonStringOfMovieDtoWithoutLink()));
    }

    @Test
    @DisplayName("Delete movie test")
    @WithMockUser(roles = {"MANAGER"})
    void deleteMovieTest() throws Exception {
        doNothing().when(movieService).deleteMovieById(any(UUID.class));

        mockMvc
                .perform(
                        delete("/movies/" + mockedMovieDto.getId())
                )
                .andExpect(status().isNoContent())
                .andExpect(status().reason("Movie with given Id successfully deleted"));
    }

    // The Employee DTO object instance without the property "links"
    // to prevent mismatch, due to Hateoas support
    private String getJsonStringOfMovieDtoWithoutLink() throws JsonProcessingException {
        ObjectNode objectNode = mapper.readTree(mapper.writeValueAsString(mockedMovieDto)).deepCopy();
        return objectNode.without("links").toPrettyString();
    }
}
