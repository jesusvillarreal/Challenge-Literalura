package com.Challenge.Literalura.repository;

import com.Challenge.Literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNameAndBirthYearAndDeathYear(String name, int birthYear, int deathYear);
    List<Autor> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(int year1, int year2);
}
