package com.hbsmoura.videorentalshop.service;

import com.hbsmoura.videorentalshop.dtos.MovieDto;
import com.hbsmoura.videorentalshop.enums.EnumMovieGenre;
import com.hbsmoura.videorentalshop.exceptions.MovieNotFoundException;
import com.hbsmoura.videorentalshop.exceptions.NoSuchGenreException;
import com.hbsmoura.videorentalshop.model.Movie;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    private Movie mockedMovie = Movie.builder()
            .id(UUID.randomUUID())
            .title("Movie Title")
            .direction("Director")
            .duration(60)
            .year(2022)
            .info("Some info...")
            .totalQuantity(3)
            .quantityAvailable(2)
            .valuePerDay(1.2)
            .genres(new HashSet<>())
            .themes(new HashSet<>())
            .build();

    private Optional<Movie> mockedOptionalMovie = Optional.of(mockedMovie);
    private Page<Movie> mockedPageMovies = new PageImpl<>(Collections.singletonList(mockedMovie));

    private MovieDto mockedMovieDto = new ModelMapper().map(mockedMovie, MovieDto.class);

    @Test
    @DisplayName("Create movie test")
    void createMovieTest() {
        doReturn(mockedMovie).when(movieRepository).save(any(Movie.class));

        MovieDto returnedMovie = movieService.createMovie(mockedMovieDto);

        assertThat(returnedMovie.getId(), is(mockedMovie.getId()));
        assertThat(returnedMovie.getTitle(), is(mockedMovie.getTitle()));
        assertThat(returnedMovie.getDirection(), is(mockedMovie.getDirection()));
        assertThat(returnedMovie.getDuration(), is(mockedMovie.getDuration()));
        assertThat(returnedMovie.getYear(), is(mockedMovie.getYear()));
        assertThat(returnedMovie.getInfo(), is(mockedMovie.getInfo()));
        assertThat(returnedMovie.getTotalQuantity(), is(mockedMovie.getTotalQuantity()));
        assertThat(returnedMovie.getQuantityAvailable(), is(mockedMovie.getQuantityAvailable()));
        assertThat(returnedMovie.getValuePerDay(), is(mockedMovie.getValuePerDay()));
        assertThat(returnedMovie.getGenres(), empty());
        assertThat(returnedMovie.getThemes(), empty());
    }

    @Test
    @DisplayName("Paging list of movies test")
    void listMoviesTest() {
        doReturn(mockedPageMovies).when(movieRepository).findAll(any(Pageable.class));

        Page<MovieDto> returnedPage = movieService.listMovies(mockedPageMovies.getPageable());

        assertThat(returnedPage.getTotalElements(), is(1L));
        assertThat(returnedPage.getContent().get(0), is(mockedMovieDto));
    }

    @Test
    @DisplayName("Find movie by the given id test")
    void getMovieById() {
        doReturn(mockedOptionalMovie).when(movieRepository).findById(any(UUID.class));

        MovieDto returnedMovie = movieService.getMovieById(UUID.randomUUID());

        assertThat(returnedMovie.getId(), is(mockedMovie.getId()));
        assertThat(returnedMovie.getTitle(), is(mockedMovie.getTitle()));
        assertThat(returnedMovie.getDirection(), is(mockedMovie.getDirection()));
        assertThat(returnedMovie.getDuration(), is(mockedMovie.getDuration()));
        assertThat(returnedMovie.getYear(), is(mockedMovie.getYear()));
        assertThat(returnedMovie.getInfo(), is(mockedMovie.getInfo()));
        assertThat(returnedMovie.getTotalQuantity(), is(mockedMovie.getTotalQuantity()));
        assertThat(returnedMovie.getQuantityAvailable(), is(mockedMovie.getQuantityAvailable()));
        assertThat(returnedMovie.getValuePerDay(), is(mockedMovie.getValuePerDay()));
        assertThat(returnedMovie.getGenres(), empty());
        assertThat(returnedMovie.getThemes(), empty());
    }

    @Test
    @DisplayName("Find movie by the given id throw MovieNotFoundException test")
    void getMovieByIdThrowMovieNotFoundExceptionTest() {
        doReturn(Optional.empty()).when(movieRepository).findById(any(UUID.class));

        assertThrows(MovieNotFoundException.class, () -> movieService.getMovieById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Search movies by title, director or info test")
    void searchMoviesByTitleOrDirectionOrInfo() {
        doReturn(mockedPageMovies).when(movieRepository)
                .findByTitleContainingIgnoreCaseOrDirectionContainingIgnoreCaseOrInfoContainingIgnoreCase(anyString(), anyString(), anyString(), any(Pageable.class));

        Page<MovieDto> returnedPage = movieService.searchMoviesByTitleOrDirectionOrInfo("text", PageRequest.ofSize(mockedPageMovies.getSize()));

        assertThat(returnedPage.getTotalElements(), is(1L));
        assertThat(returnedPage.getContent().get(0), is(mockedMovieDto));
    }

    @Test
    @DisplayName("Search movies by genre test")
    void searchMoviesByGenreTest() {
        doReturn(mockedPageMovies).when(movieRepository)
                .findByGenres(any(EnumMovieGenre.class), any(Pageable.class));

        Page<MovieDto> returnedPage = movieService.searchMoviesByGenre("action", mockedPageMovies.getPageable());

        assertThat(returnedPage.getTotalElements(), is(1L));
        assertThat(returnedPage.getContent().get(0), is(mockedMovieDto));
    }

    @Test
    @DisplayName("Search movies by genre throws NoSuchGenreException test")
    void searchMoviesByGenreThrowsNoSuchGenreExceptionTest() {
        assertThrows(NoSuchGenreException.class, () -> movieService.searchMoviesByGenre("invalid genre", mockedPageMovies.getPageable()));
    }

    @Test
    @DisplayName("Search movies by theme test")
    void searchMoviesByThemeTest() {
        doReturn(mockedPageMovies).when(movieRepository)
                .findByThemesIgnoreCase(anyString(), any(Pageable.class));

        Page<MovieDto> returnedPage = movieService.searchMoviesByTheme("theme", mockedPageMovies.getPageable());

        assertThat(returnedPage.getTotalElements(), is(1L));
        assertThat(returnedPage.getContent().get(0), is(mockedMovieDto));
    }

    @Test
    @DisplayName("Update movie test")
    void updateMovieTest() {
        doReturn(mockedOptionalMovie).when(movieRepository).findById(any(UUID.class));
        doReturn(mockedMovie).when(movieRepository).save(any(Movie.class));

        MovieDto returnedMovie = movieService.updateMovie(mockedMovieDto);

        assertThat(returnedMovie.getId(), is(mockedMovie.getId()));
        assertThat(returnedMovie.getTitle(), is(mockedMovie.getTitle()));
        assertThat(returnedMovie.getDirection(), is(mockedMovie.getDirection()));
        assertThat(returnedMovie.getDuration(), is(mockedMovie.getDuration()));
        assertThat(returnedMovie.getYear(), is(mockedMovie.getYear()));
        assertThat(returnedMovie.getInfo(), is(mockedMovie.getInfo()));
        assertThat(returnedMovie.getTotalQuantity(), is(mockedMovie.getTotalQuantity()));
        assertThat(returnedMovie.getQuantityAvailable(), is(mockedMovie.getQuantityAvailable()));
        assertThat(returnedMovie.getValuePerDay(), is(mockedMovie.getValuePerDay()));
        assertThat(returnedMovie.getGenres(), empty());
        assertThat(returnedMovie.getThemes(), empty());
    }

    @Test
    @DisplayName("Update movie throw MovieNotFoundException test")
    void updateMovieThrowMovieNotFoundExceptionTest() {
        doReturn(Optional.empty()).when(movieRepository).findById(any(UUID.class));

        assertThrows(MovieNotFoundException.class, () -> movieService.updateMovie(mockedMovieDto));
    }

    @Test
    @DisplayName("Delete movie by id test")
    void deleteMovieByIdTest() {
        doReturn(mockedOptionalMovie).when(movieRepository).findById(any(UUID.class));
        doNothing().when(movieRepository).delete(any(Movie.class));

        movieService.deleteMovieById(UUID.randomUUID());

        verify(movieRepository, times(1)).findById(any(UUID.class));
        verify(movieRepository, times(1)).delete(any(Movie.class));
    }

    @Test
    @DisplayName("Delete movie by id throw MovieNotFoundException test")
    void deleteMovieByIdThrowMovieNotFoundExceptionTest() {
        doReturn(Optional.empty()).when(movieRepository).findById(any(UUID.class));

        assertThrows(MovieNotFoundException.class, () -> movieService.deleteMovieById(UUID.randomUUID()));
    }
}
