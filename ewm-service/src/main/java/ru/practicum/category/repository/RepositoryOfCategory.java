package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.model.Category;

public interface RepositoryOfCategory extends JpaRepository<Category, Long> {

    Category getById(Long catId);

    Category findCategoryById(Long catId);

    void deleteCategoryById(Long catId);
}
