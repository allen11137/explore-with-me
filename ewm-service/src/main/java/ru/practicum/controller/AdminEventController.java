package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.AdminUpdateEventRequest;
import ru.practicum.event.dto.EventCompleteDto;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventCompleteDto>> getEventsAdmin(@RequestParam(required = false) List<Long> users,
                                                                 @RequestParam(required = false) List<String> states,
                                                                 @RequestParam(required = false) List<Long> categories,
                                                                 @RequestParam(required = false) String rangeStart,
                                                                 @RequestParam(required = false) String rangeEnd,
                                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return new ResponseEntity<>(eventService.getAdminEvents(users, states, categories, rangeStart, rangeEnd,
                from, size), HttpStatus.OK);
    }


    @PatchMapping("/{eventId}")
    public ResponseEntity<EventCompleteDto> updateEventAdmin(@NotNull @PathVariable(required = false) Long eventId,
                                                             @NotNull @Valid @RequestBody AdminUpdateEventRequest updateEventAdminRequest) {
        return new ResponseEntity<>(eventService.updateAdminEvent(eventId, updateEventAdminRequest), HttpStatus.OK);
    }
}
