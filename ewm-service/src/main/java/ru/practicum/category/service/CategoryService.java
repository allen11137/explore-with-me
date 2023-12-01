package ru.practicum.category.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.AddCategoryDto;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.MapperOfCategory;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.RepositoryOfCategory;
import ru.practicum.event.repository.RepositoryOfEvent;
import ru.practicum.exception.DoubleNameException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.CategoryValidationException;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class CategoryService {
    private final RepositoryOfCategory repositoryOfCategory;
    private final RepositoryOfEvent repositoryOfEvent;

    @Transactional
    public List<CategoryDto> getPublicCategory(Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);
        return repositoryOfCategory.findAll(pageable)
                .stream()
                .map(MapperOfCategory::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDto getPublicCategoryById(Long catId) {
        Category category = repositoryOfCategory.findCategoryById(catId);
        if (category == null) {
            throw new NotFoundException("Категория не найдена.");
        }
        return MapperOfCategory.toCategoryDto(category);
    }

    @Transactional
    public CategoryDto addAdminCategory(AddCategoryDto addCategoryDto) {
        Category category = MapperOfCategory.toCategory(addCategoryDto);
        try {
            return MapperOfCategory.toCategoryDto(repositoryOfCategory.save(category));
        } catch (DataIntegrityViolationException e) {
            throw new DoubleNameException("Название категории уже существует");
        }
    }

    @Transactional
    public CategoryDto updateAdminCategory(Long catId, AddCategoryDto addCategoryDto) {
        getCategory(catId);
        Category newCategory = MapperOfCategory.toCategory(addCategoryDto);
        newCategory.setId(catId);
        CategoryDto categoryDto;
        try {
            categoryDto = MapperOfCategory.toCategoryDto(repositoryOfCategory.saveAndFlush(newCategory));
        } catch (DataIntegrityViolationException e) {
            throw new DoubleNameException("Название категории уже существует");
        }
        return categoryDto;
    }

    @Transactional
    public void deleteAdminCategory(Long catId) {
        getCategory(catId);
        if (repositoryOfEvent.findFirstByCategoryId(catId) != null) {
            throw new CategoryValidationException("Ошибка. Категория не была удалена.");
        }
        repositoryOfCategory.deleteCategoryById(catId);
    }


    private Category getCategory(Long categoryId) {
        Category category = repositoryOfCategory.getById(categoryId);
        if (category == null) {
            throw new NotFoundException("Категория не найдена.");
        }
        return category;
    }
}