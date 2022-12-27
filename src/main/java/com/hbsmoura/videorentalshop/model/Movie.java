package com.hbsmoura.videorentalshop.model;

import com.hbsmoura.videorentalshop.enums.EnumMovieGenre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

/**
* The Movie class represents a technical sheet from a movie.
* */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String direction;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private int year;

    private String info;

    @Column(nullable = false)
    private int totalQuantity;

    @Column(nullable = false)
    private int quantityAvailable;

    @Column(nullable = false)
    private double valuePerDay;

    @Column
    @Enumerated
    @ElementCollection(targetClass = EnumMovieGenre.class)
    private Set<EnumMovieGenre> genres;

    @Column
    @ElementCollection
    @CollectionTable(name="tb_themes", joinColumns=@JoinColumn(name="movie_id"))
    private Set<String> themes;

}
