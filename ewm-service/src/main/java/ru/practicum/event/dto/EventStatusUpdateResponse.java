package ru.practicum.event.dto;

import lombok.*;
import lombok.experimental.Accessors;
import ru.practicum.participant.dto.ParticipantRequestDto;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventStatusUpdateResponse {

    private List<ParticipantRequestDto> acceptRequests;
    private List<ParticipantRequestDto> cancelledRequests;
}