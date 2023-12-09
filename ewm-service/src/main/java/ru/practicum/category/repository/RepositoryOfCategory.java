package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.category.model.Category;

import java.util.Optional;

@Repository
public interface RepositoryOfCategory extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);

    void deleteById(Long id);
}
