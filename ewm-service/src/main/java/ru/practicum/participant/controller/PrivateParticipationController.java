package ru.practicum.participant.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participant.dto.ParticipantRequestDto;
import ru.practicum.participant.service.ParticipantService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class PrivateParticipationController {
    private final ParticipantService participantService;

    @PostMapping
    public ResponseEntity<ParticipantRequestDto> addParticipationRequestPrivate(@Positive @PathVariable(required = false) Long userId,
                                                                                @Positive @RequestParam(required = false) Long eventId) {
        return new ResponseEntity<>(participantService.addPrivateParticipationRequest(userId, eventId),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ParticipantRequestDto>> getParticipationRequestPrivate(@NotNull @Positive @PathVariable Long userId) {
        return new ResponseEntity<>(participantService.getListOfParticipationRequest(userId), HttpStatus.OK);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipantRequestDto> updateRejectedParticipationRequestPrivate(@NotNull @Positive @PathVariable Long userId,
                                                                                           @NotNull @Positive @PathVariable(required = true,
                                                                                                   name = "requestId") Long requestId) {
        return new ResponseEntity<>(participantService.updatePrivateRejectedParticipationRequest(userId, requestId),
                HttpStatus.OK);
    }
}
