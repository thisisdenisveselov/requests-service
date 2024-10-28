package ru.veselov.requests_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.veselov.requests_service.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByNameContainingIgnoreCase(String name);
    Optional<Person> findByUsername(String username);
}
