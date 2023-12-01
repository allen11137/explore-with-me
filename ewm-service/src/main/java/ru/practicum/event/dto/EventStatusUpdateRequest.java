package ru.practicum.event.dto;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventStatusUpdateRequest {
    @NotEmpty
    private List<Long> requestIds;

    @NotBlank
    private String status;
}