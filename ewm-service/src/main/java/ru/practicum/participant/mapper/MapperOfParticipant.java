package ru.practicum.participant.mapper;

import ru.practicum.participant.dto.ParticipantRequestDto;
import ru.practicum.participant.model.Participant;

import java.time.format.DateTimeFormatter;

import static org.hibernate.type.descriptor.java.DateTypeDescriptor.DATE_FORMAT;

public class MapperOfParticipant {
    public static ParticipantRequestDto toParticipationRequestDto(Participant participationRequest) {
        return new ParticipantRequestDto()
                .setId(participationRequest.getId())
                .setCreated(participationRequest.getCreated().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .setEvent(participationRequest.getEvent())
                .setRequester(participationRequest.getRequester())
                .setStatus(participationRequest.getStatus().toString());
    }
}