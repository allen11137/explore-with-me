package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsDto;
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

    @PostMapping(path = "/hit")
    public ResponseEntity<Stats> addHit(@Validated @RequestBody EndpointHitDto endpointHitDto) {
        return ResponseEntity.ok(statService.createStatHit(endpointHitDto));
    }

    @GetMapping(path = "/stats")
    public ResponseEntity<List<StatsDto>> getListOfStats(@RequestParam @DateTimeFormat(pattern = DATA_TIME_PATTERN) LocalDateTime start,
                                                         @RequestParam @DateTimeFormat(pattern = DATA_TIME_PATTERN) LocalDateTime end,
                                                         @RequestParam(required = false) Collection<String> uris,
                                                         @RequestParam(defaultValue = "false") boolean unique
    ) {
        return ResponseEntity.ok(statService.getStatHit(start, end, uris, unique));
    }
}