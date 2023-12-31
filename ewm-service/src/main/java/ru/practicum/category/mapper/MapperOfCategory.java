package ru.practicum.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.category.dto.AddCategoryDto;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;

@UtilityClass
public class MapperOfCategory {
    public static Category toCategory(AddCategoryDto addCategoryDto) {
        return new Category()
                .setName(addCategoryDto.getName());
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto()
                .setId(category.getId())
                .setName(category.getName());
    }
}
