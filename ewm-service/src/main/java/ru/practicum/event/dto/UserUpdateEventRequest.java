package ru.practicum.event.dto;

import lombok.*;
import lombok.experimental.Accessors;
import ru.practicum.location.model.Location;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateEventRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    @Positive
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    @Positive
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    @Size(min = 3, max = 120)
    private String title;
}