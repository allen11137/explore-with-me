package ru.practicum.compilation.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.AddCompilationDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.RequestUpdateCompilation;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/admin/compilations")
@Validated
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> addCompilationAdmin(@Valid @RequestBody AddCompilationDto addCompilationDto) {
        return new ResponseEntity<>(compilationService.addAdminCompilation(addCompilationDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilationByIdAdmin(@Positive @PathVariable Long compId,
                                                                     @Valid @RequestBody RequestUpdateCompilation requestUpdateCompilation) {
        return new ResponseEntity<>(compilationService.updateAdminCompilationById(compId, requestUpdateCompilation),
                HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{compId}")
    public void deleteCompilationByIdAdmin(@Positive @PathVariable("compId") Long compId) {
        compilationService.deleteAdminCompilationById(compId);
    }
}