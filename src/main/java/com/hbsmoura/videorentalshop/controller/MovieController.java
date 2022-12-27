package com.hbsmoura.videorentalshop.controller;

import com.hbsmoura.videorentalshop.dtos.MovieDto;
import com.hbsmoura.videorentalshop.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieDto createMovie(@RequestBody MovieDto givenMovie) {
        return movieService.createMovie(givenMovie);
    }

    @GetMapping
    public Page<MovieDto> listMovies(Pageable pageable) {
        return movieService.listMovies(pageable);
    }

    @GetMapping("/{id}")
    public MovieDto getMovieById(@PathVariable("id") UUID id) {
        return movieService.getMovieById(id);
    }

    @GetMapping("/search/{text}")
    public Page<MovieDto> searchMoviesByTitleDirectionOrInfo(@PathVariable("text") String text, Pageable pageable) {
        return movieService.searchMoviesByTitleOrDirectionOrInfo(text, pageable);
    }

    @GetMapping("/search/genre/{genre}")
    public Page<MovieDto> searchMoviesByGenre(@PathVariable("genre") String genre, Pageable pageable) {
        return movieService.searchMoviesByGenre(genre, pageable);
    }

    @GetMapping("/search/theme/{theme}")
    public Page<MovieDto> searchMoviesByTheme(@PathVariable("theme") String theme, Pageable pageable) {
        return movieService.searchMoviesByTheme(theme, pageable);
    }

    @PutMapping
    public MovieDto updateMovie(@RequestBody MovieDto givenMovie) {
        return movieService.updateMovie(givenMovie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Movie with given Id successfully deleted")
    public void deleteMovie(@PathVariable("id") UUID id) {
        movieService.deleteMovieById(id);
    }
}
