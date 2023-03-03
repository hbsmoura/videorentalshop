package com.hbsmoura.videorentalshop.controller;

import com.hbsmoura.videorentalshop.dtos.MovieDto;
import com.hbsmoura.videorentalshop.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/movies")
@PreAuthorize("permitAll()")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('EMPLOYEE')")
    public MovieDto createMovie(@RequestBody @Valid MovieDto givenMovie) {
        return movieService.createMovie(givenMovie);
    }

    @GetMapping
    public Page<MovieDto> listMovies(Pageable pageable) {
        return movieService.listMovies(pageable);
    }

    @GetMapping("/{id}")
    public MovieDto getMovieById(@PathVariable UUID id) {
        return movieService.getMovieById(id);
    }

    @GetMapping("/search/{text}")
    public Page<MovieDto> searchMoviesByTitleDirectionOrInfo(@PathVariable String text, Pageable pageable) {
        return movieService.searchMoviesByTitleOrDirectionOrInfo(text, pageable);
    }

    @GetMapping("/search/genre/{genre}")
    public Page<MovieDto> searchMoviesByGenre(@PathVariable String genre, Pageable pageable) {
        return movieService.searchMoviesByGenre(genre, pageable);
    }

    @GetMapping("/search/theme/{theme}")
    public Page<MovieDto> searchMoviesByTheme(@PathVariable String theme, Pageable pageable) {
        return movieService.searchMoviesByTheme(theme, pageable);
    }

    @PutMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public MovieDto updateMovie(@RequestBody @Valid MovieDto givenMovie) {
        return movieService.updateMovie(givenMovie);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Movie with given Id successfully deleted")
    public void deleteMovie(@PathVariable UUID id) {
        movieService.deleteMovieById(id);
    }
}
