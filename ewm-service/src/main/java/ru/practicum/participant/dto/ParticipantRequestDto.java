package ru.practicum.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantRequestDto {
    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private String status;
}