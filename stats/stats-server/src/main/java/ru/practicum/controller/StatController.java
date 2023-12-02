package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.model.Stats;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static ru.practicum.EndpointHitDto.DATA_TIME_PATTERN;

@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public Stats saveHit(@RequestBody EndpointHitDto endpointHitDto) {
        return statService.createStatHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<Stats> getStats(@RequestParam @DateTimeFormat(pattern = DATA_TIME_PATTERN) LocalDateTime start,
                                    @RequestParam @DateTimeFormat(pattern = DATA_TIME_PATTERN) LocalDateTime end,
                                    @RequestParam(required = false) Collection<String> uris,
                                    @RequestParam(defaultValue = "false") boolean unique) {
        return statService.getStatHit(start, end, uris, unique);
    }
}