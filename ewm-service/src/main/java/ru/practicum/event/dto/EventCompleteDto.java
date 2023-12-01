package ru.practicum.event.dto;

import lombok.*;
import lombok.experimental.Accessors;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.dto.UserDto;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCompleteDto {
    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private Boolean paid;
    private String eventDate;
    private UserDto initiator;
    private Long confirmedRequests;
    private String description;
    private Integer participantLimit;
    private String state;
    private String createdOn;
    private LocationDto location;
    private Boolean requestModeration;
    private String publishedOn;
    private Long views;
}