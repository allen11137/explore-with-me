package ru.practicum.compilation.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/compilations")
public class PublicCompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilationsPublic(@RequestParam(required = false, name = "pinned") Boolean pinned,
                                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                      @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return new ResponseEntity<>(compilationService.getPublicCompilations(pinned, from, size), HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilationByIdPublic(@Positive @PathVariable Long compId) {
        return new ResponseEntity<>(compilationService.getPublicCompilationById(compId), HttpStatus.OK);
    }
}