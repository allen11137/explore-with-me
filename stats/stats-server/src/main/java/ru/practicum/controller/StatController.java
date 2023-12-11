package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsView;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static ru.practicum.mapper.MapperOfStats.DATA_TIME_PATTERN;

@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public EndpointHitDto addHit(@RequestBody EndpointHitDto endpointHitDto) {
        return statService.createStatHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<StatsView> getListOfStats(@RequestParam @DateTimeFormat(pattern = DATA_TIME_PATTERN) LocalDateTime start,
                                          @RequestParam @DateTimeFormat(pattern = DATA_TIME_PATTERN) LocalDateTime end,
                                          @RequestParam(required = false) Collection<String> uris,
                                          @RequestParam(defaultValue = "false") boolean unique) {
        return statService.getListOfStatHit(start, end, uris, unique);
    }
}
