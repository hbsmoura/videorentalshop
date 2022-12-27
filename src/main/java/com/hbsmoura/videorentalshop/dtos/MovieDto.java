package com.hbsmoura.videorentalshop.dtos;

import com.hbsmoura.videorentalshop.enums.EnumMovieGenre;
import com.hbsmoura.videorentalshop.model.Movie;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto extends RepresentationModel<MovieDto> {

    private UUID id;

    @NotBlank
    private String title;

    @NotBlank
    private String direction;

    @NotBlank
    private int duration;

    @NotBlank
    private int year;

    private String info;

    @NotBlank
    private int totalQuantity;

    @NotBlank
    private int quantityAvailable;

    @NotBlank
    private double valuePerDay;

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
