package com.hbsmoura.videorentalshop.controller;

import com.hbsmoura.videorentalshop.config.apiresponse.ApiResponseForbidden;
import com.hbsmoura.videorentalshop.config.apiresponse.ApiResponseNotFound;
import com.hbsmoura.videorentalshop.config.apiresponse.ApiResponseOk;
import com.hbsmoura.videorentalshop.config.apiresponse.ApiResponseUnauthorized;
import com.hbsmoura.videorentalshop.config.hateoas.HateoasLink;
import com.hbsmoura.videorentalshop.dtos.MovieDto;
import com.hbsmoura.videorentalshop.service.MovieService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/movies")
@PreAuthorize("permitAll()")
@Tag(name = "Movie Controller")
public class MovieController {

    private final MovieService movieService;

    private static class PageOfMovieDto extends PageImpl<MovieDto> {
        public PageOfMovieDto(List<MovieDto> content) {
            super(content);
        }
    }

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_EMPLOYEE"})
    @Operation(
            summary = "Create movie",
            description = "Creates a new movie, saves on database and retrieves it"
    )
    @ApiResponseUnauthorized @ApiResponseForbidden
    @HateoasLink(relation = "Create", requestType = "POST")
    public MovieDto createMovie(@RequestBody @Valid MovieDto givenMovie) {
        return movieService.createMovie(givenMovie);
    }

    @GetMapping
    @Operation(
            summary = "List movies",
            description = "Retrieves a paged list of movies"
    )
    @HateoasLink(relation = "List")
    public Page<MovieDto> listMovies(Pageable pageable) {
        return movieService.listMovies(pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get movie by id",
            description = "Retrieves a movie by its id"
    )
    @ApiResponseOk(content = @Content(schema = @Schema(implementation = MovieDto.class)))
    @ApiResponseNotFound
    @HateoasLink(selfRel = true)
    public MovieDto getMovieById(@PathVariable UUID id) {
        return movieService.getMovieById(id);
    }

    @GetMapping("/search/{text}")
    @Operation(
            summary = "Search movies by title, direction, or info",
            description = "Retrieves a list of movies according to the given parameter text"
    )
    @HateoasLink(relation = "Search by title, direction, or info")
    public Page<MovieDto> searchMoviesByTitleDirectionOrInfo(@PathVariable String text, Pageable pageable) {
        return movieService.searchMoviesByTitleOrDirectionOrInfo(text, pageable);
    }

    @GetMapping("/search/genre/{genre}")
    @Operation(
            summary = "Search movies by genre",
            description = "Retrieves a list of movies according to the given genre"
    )
    @ApiResponseOk(content = @Content(schema = @Schema(implementation = PageOfMovieDto.class)))
    @ApiResponseNotFound
    @HateoasLink(relation = "Search by genre")
    public Page<MovieDto> searchMoviesByGenre(@PathVariable String genre, Pageable pageable) {
        return movieService.searchMoviesByGenre(genre, pageable);
    }

    @GetMapping("/search/theme/{theme}")
    @Operation(
            summary = "Search movies by theme",
            description = "Retrieves a list of movies according to the given theme"
    )
    @HateoasLink(relation = "Search by theme")
    public Page<MovieDto> searchMoviesByTheme(@PathVariable String theme, Pageable pageable) {
        return movieService.searchMoviesByTheme(theme, pageable);
    }

    @PutMapping
    @Secured({"ROLE_EMPLOYEE"})
    @Operation(
            summary = "Update movie",
            description = "Updates the movie data"
    )
    @ApiResponseOk(content = @Content(schema = @Schema(implementation = MovieDto.class)))
    @ApiResponseNotFound @ApiResponseUnauthorized @ApiResponseForbidden
    @HateoasLink(relation = "Update", requestType = "PUT")
    public MovieDto updateMovie(@RequestBody @Valid MovieDto givenMovie) {
        return movieService.updateMovie(givenMovie);
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_MANAGER"})
    @Operation(
            summary = "Delete movie",
            description = "Deletes a movie from database"
    )
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Movie with given Id successfully deleted")
    @ApiResponseNotFound @ApiResponseUnauthorized @ApiResponseForbidden
    @HateoasLink(relation = "Delete", requestType = "DELETE")
    public void deleteMovie(@PathVariable UUID id) {
        movieService.deleteMovieById(id);
    }
}
