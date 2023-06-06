package com.hbsmoura.videorentalshop.dtos;

import com.hbsmoura.videorentalshop.config.hateoas.HateoasIdentity;
import com.hbsmoura.videorentalshop.config.hateoas.HateoasModel;
import com.hbsmoura.videorentalshop.controller.MovieController;
import com.hbsmoura.videorentalshop.enums.EnumMovieGenre;
import com.hbsmoura.videorentalshop.model.Movie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@HateoasModel(controller = MovieController.class)
public class MovieDto extends RepresentationModel<MovieDto> implements Serializable {
    private static final long serialVersionUID = 1L;

    @HateoasIdentity
    private UUID id;

    @NotBlank
    private String title;

    @NotBlank
    private String direction;

    @NotNull
    private int duration;

    @NotNull
    private int year;

    private String info;

    @NotNull
    private int totalQuantity;

    @NotNull
    private int quantityAvailable;

    @NotNull
    private BigDecimal valuePerDay;

    private Set<EnumMovieGenre> genres;

    private Set<String> themes;

    public MovieDto(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.direction = movie.getDirection();
        this.duration = movie.getDuration();
        this.year = movie.getYear();
        this.info = movie.getInfo();
        this.totalQuantity = movie.getTotalQuantity();
        this.quantityAvailable = movie.getQuantityAvailable();
        this.valuePerDay = movie.getValuePerDay();
        this.genres = movie.getGenres();
        this.themes = movie.getThemes();
    }
}
