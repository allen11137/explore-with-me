package ru.practicum.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.location.model.Location;


public interface RepositoryOfLocation extends JpaRepository<Location, Long> {

}