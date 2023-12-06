package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.practicum.event.dto.EventBriefDto;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class CompilationDto {
    private Long id;
    private List<EventBriefDto> events;
    private boolean pinned;
    private String title;
}
