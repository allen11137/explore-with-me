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

@RestController
@RequiredArgsConstructor
public class StatController {
    public static final String DATA_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final StatService statService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public EndpointHitDto saveHit(@RequestBody EndpointHitDto endpointHitDto) {
        return statService.createStatHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<StatsView> getStats(@RequestParam @DateTimeFormat(pattern = DATA_TIME_PATTERN) LocalDateTime start,
                                    @RequestParam @DateTimeFormat(pattern = DATA_TIME_PATTERN) LocalDateTime end,
                                    @RequestParam(required = false) Collection<String> uris,
                                    @RequestParam(defaultValue = "false") boolean unique) {
        return statService.getStatHit(start, end, uris, unique);
    }
}