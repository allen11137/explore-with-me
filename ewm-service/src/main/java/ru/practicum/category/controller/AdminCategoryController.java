package ru.practicum.category.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.AddCategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/admin/categories")
@Validated
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> addCategoryAdmin(@Valid @RequestBody AddCategoryDto addCategoryDto) {
        return new ResponseEntity<>(categoryService.addAdminCategory(addCategoryDto), CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<?> updateCategoryAdmin(@Positive @PathVariable Long catId,
                                                 @Valid @RequestBody AddCategoryDto addCategoryDto) {
        return new ResponseEntity<>(categoryService.updateAdminCategory(catId, addCategoryDto), OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{catId}")
    public void deleteCategoryAdmin(@Positive @PathVariable("catId") Long catId) {
        categoryService.deleteAdminCategory(catId);
    }
}