package ru.practicum.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class StatsView {
    private String app;
    private String uri;
    private Integer hits;
}
