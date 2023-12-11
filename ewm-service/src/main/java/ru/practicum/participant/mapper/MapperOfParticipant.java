package ru.practicum.participant.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.participant.dto.ParticipantRequestDto;
import ru.practicum.participant.model.Participant;

import java.time.format.DateTimeFormatter;

import static ru.practicum.event.Constant.DATA_TIME_PATTERN;

@UtilityClass
public class MapperOfParticipant {
    public ParticipantRequestDto toParticipationRequestDto(Participant participationRequest) {
        return new ParticipantRequestDto()
                .setId(participationRequest.getId())
                .setCreated(participationRequest.getCreated().format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)))
                .setEvent(participationRequest.getEvent())
                .setRequester(participationRequest.getRequester())
                .setStatus(participationRequest.getStatus().toString());
    }
}