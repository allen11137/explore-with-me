package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.participant.dto.ParticipantRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventCompleteDto> addEvent(@Positive @PathVariable Long userId,
                                                     @NotNull @Valid @RequestBody AddEventDto newEventDto) {
        return new ResponseEntity<>(eventService.addPrivateEvent(userId, newEventDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventBriefDto>> getEvents(@NotNull @Positive @PathVariable(required = false) Long userId,
                                                         @PositiveOrZero
                                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @Positive
                                                         @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return new ResponseEntity<>(eventService.getPrivateEvents(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventCompleteDto> getEvent(@Positive @PathVariable(required = false) Long userId,
                                                     @Positive @PathVariable(required = false) Long eventId) {
        return new ResponseEntity<>(eventService.getPrivateEvent(userId, eventId), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipantRequestDto>> getRequestsEventsUser(@Positive @PathVariable Long userId,
                                                                             @Positive @PathVariable Long eventId) {
        return new ResponseEntity<>(eventService.getPrivateRequestsEventsUser(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventCompleteDto> updateEventUserRequest(@Positive @PathVariable(required = false) Long userId,
                                                                   @Positive @PathVariable(required = false) Long eventId,
                                                                   @Valid @RequestBody UserUpdateEventRequest updateEventUserRequest) {
        return new ResponseEntity<>(eventService.updatePrivateEvent(userId, eventId, updateEventUserRequest),
                HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventStatusUpdateResponse> updateEventRequestStatus(@Positive @PathVariable Long userId,
                                                                              @Positive @PathVariable Long eventId,
                                                                              @Valid @RequestBody EventStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return new ResponseEntity<>(eventService.updatePrivateEventRequestStatus(
                userId, eventId, eventRequestStatusUpdateRequest), HttpStatus.OK);
    }
}