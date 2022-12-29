package com.hbsmoura.videorentalshop.model;

import com.hbsmoura.videorentalshop.enums.EnumMovieGenre;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
* The Movie class represents a technical sheet from a movie.
* */

@Entity
@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Movie movie = (Movie) o;
        return id != null && Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", direction='" + direction + '\'' +
                ", duration=" + duration +
                ", year=" + year +
                ", info='" + info + '\'' +
                ", totalQuantity=" + totalQuantity +
                ", quantityAvailable=" + quantityAvailable +
                ", valuePerDay=" + valuePerDay +
                ", genres=" + genres +
                ", themes=" + themes +
                '}';
    }
}
