package ru.practicum.event.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventBriefDto;
import ru.practicum.event.dto.EventCompleteDto;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.event.Constant.DATA_TIME_PATTERN;


@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/events")
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventBriefDto>> getPublicEventsAndStats(HttpServletRequest request,
                                                                       @RequestParam(required = false) String text,
                                                                       @RequestParam(required = false) List<Long> categories,
                                                                       @RequestParam(required = false) Boolean paid,
                                                                       @DateTimeFormat(pattern = DATA_TIME_PATTERN)
                                                                       @RequestParam(required = false) LocalDateTime rangeStart,
                                                                       @DateTimeFormat(pattern = DATA_TIME_PATTERN)
                                                                       @RequestParam(required = false) LocalDateTime rangeEnd,
                                                                       @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                                       @RequestParam(required = false) String sort,
                                                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                       @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return new ResponseEntity<>(eventService.getPublicEventsAndStats(request, text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size), HttpStatus.OK);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<EventCompleteDto> getPublicEventByIdAndStats(HttpServletRequest request,
                                                                       @Positive @PathVariable("Id") Long eventId) {
        return new ResponseEntity<>(eventService.getPublicEventByIdAndStats(request, eventId), HttpStatus.OK);
    }
}