package com.hbsmoura.videorentalshop.repository;

import com.hbsmoura.videorentalshop.enums.EnumMovieGenre;
import com.hbsmoura.videorentalshop.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {
    Page<Movie> findByTitleContainingIgnoreCaseOrDirectionContainingIgnoreCaseOrInfoContainingIgnoreCase(
            String textTitle, String textDirection, String textInfo, Pageable pageable
    );

    Page<Movie> findByGenres(EnumMovieGenre genre, Pageable pageable);

    Page<Movie> findByThemesIgnoreCase(String text, Pageable pageable);
}
