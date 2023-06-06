package com.hbsmoura.videorentalshop.service;

import com.hbsmoura.videorentalshop.config.hateoas.LinkReferrer;
import com.hbsmoura.videorentalshop.dtos.MovieDto;
import com.hbsmoura.videorentalshop.enums.EnumMovieGenre;
import com.hbsmoura.videorentalshop.exceptions.MovieNotFoundException;
import com.hbsmoura.videorentalshop.exceptions.NoSuchGenreException;
import com.hbsmoura.videorentalshop.model.Movie;
import com.hbsmoura.videorentalshop.repository.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Method for insertion of a new movie
     * @param givenMovie the movie to be created
     * @return the created movie
     */

    public MovieDto createMovie(MovieDto givenMovie) {
        givenMovie.setId(null);
        givenMovie.setQuantityAvailable(givenMovie.getTotalQuantity());

        Movie newMovie = new ModelMapper().map(givenMovie, Movie.class);

        Movie savedMovie = movieRepository.save(newMovie);

        MovieDto savedMovieDto = new ModelMapper().map(savedMovie, MovieDto.class);

        return LinkReferrer.doRefer(savedMovieDto);
    }

    /**
     * Method for get a page of movies with the given properties.
     * @param pageable the object that carries the page properties
     * @return a page of movies from the model layer
     */

    public Page<MovieDto> listMovies(Pageable pageable)  {
        Page<Movie> movies = movieRepository.findAll(pageable);
        Page<MovieDto> moviesDto = movies.map(movie -> new ModelMapper().map(movie, MovieDto.class));

        return moviesDto.map(m -> LinkReferrer.doRefer(m));
    }

    /**
     * Method for retrieve a movie by its id.
     * @param id the given id
     * @return the found movie
     * @throws MovieNotFoundException if there is no movie with the given id on the model layer
     */

    public MovieDto getMovieById(UUID id) {
        Movie movie =  movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);
        MovieDto movieDto = new ModelMapper().map(movie, MovieDto.class);
        return LinkReferrer.doRefer(movieDto);
    }

    /**
     * Method for search movies by its title, director ou info.
     * @param text the text for the search
     * @param pageable the object that carries the page properties
     * @return a page of found movies from the model layer
     */

    public Page<MovieDto> searchMoviesByTitleOrDirectionOrInfo(String text, Pageable pageable) {
        Page<Movie> movies = movieRepository
                .findByTitleContainingIgnoreCaseOrDirectionContainingIgnoreCaseOrInfoContainingIgnoreCase(
                        text, text, text, pageable
                );
        Page<MovieDto> moviesDto = movies.map(movie -> new ModelMapper().map(movie, MovieDto.class));

        return moviesDto.map(m -> LinkReferrer.doRefer(m));
    }

    /**
     * Method for search movies by its set of genres.
     * @param givenGenre the text for search
     * @param pageable the object that carries the page properties
     * @throws NoSuchGenreException if there is no corresponding genre for the given text
     * @return a page of found movies from the model layer
     */

    public Page<MovieDto> searchMoviesByGenre(String givenGenre, Pageable pageable) {
        givenGenre = givenGenre.toUpperCase();
        try {
            EnumMovieGenre genre = EnumMovieGenre.valueOf(givenGenre);
            Page<Movie> movies = movieRepository.findByGenres(genre, pageable);
            Page<MovieDto> moviesDto = movies.map(movie -> new ModelMapper().map(movie, MovieDto.class));

            return moviesDto.map(m -> LinkReferrer.doRefer(m));
        } catch (IllegalArgumentException e) {
            throw new NoSuchGenreException(givenGenre);
        }
    }

    /**
     * Method for search movies by its list ou themes.
     * @param theme the text for the search
     * @param pageable the object that carries the page properties
     * @return a page of found movies from the model layer
     */

    public Page<MovieDto> searchMoviesByTheme(String theme, Pageable pageable) {
        Page<Movie> movies = movieRepository.findByThemesIgnoreCase(theme, pageable);
        Page<MovieDto> moviesDto = movies.map(movie -> new ModelMapper().map(movie, MovieDto.class));

        return moviesDto.map(m -> LinkReferrer.doRefer(m));
    }

    /**
     * Method for update a given movie.
     * @param givenMovie the movie to be updated
     * @return the updated movie
     * @throws MovieNotFoundException if there is no movie with the id of the given movie on the model layer
     */

    public MovieDto updateMovie(MovieDto givenMovie) {
        Movie movie = movieRepository.findById(givenMovie.getId()).orElseThrow(MovieNotFoundException::new);

        movie.setTitle(givenMovie.getTitle());
        movie.setDirection(givenMovie.getDirection());
        movie.setDuration(givenMovie.getDuration());
        movie.setYear(givenMovie.getYear());
        movie.setInfo(givenMovie.getInfo());

        int tempAvailableQuantity =
                movie.getTotalQuantity() != givenMovie.getTotalQuantity() ?
                movie.getQuantityAvailable() + (givenMovie.getTotalQuantity() - movie.getTotalQuantity()) :
                movie.getQuantityAvailable();

        movie.setQuantityAvailable(tempAvailableQuantity);
        movie.setTotalQuantity(givenMovie.getTotalQuantity());
        movie.setValuePerDay(givenMovie.getValuePerDay());
        movie.setGenres(givenMovie.getGenres());
        movie.setThemes(givenMovie.getThemes());

        movieRepository.save(movie);

        MovieDto movieDto = new ModelMapper().map(movie, MovieDto.class);
        return LinkReferrer.doRefer(movieDto);
    }

    /**
     * Method for delete a movie by a given id.
     * @param id the given id
     * @throws MovieNotFoundException if there is no movie with the given id on the model layer
     */

    public void deleteMovieById(UUID id) {
        Movie movie = movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);
        movieRepository.delete(movie);
    }
}
