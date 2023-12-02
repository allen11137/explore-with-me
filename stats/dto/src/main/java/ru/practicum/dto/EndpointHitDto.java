package ru.practicum.dto;

import lombok.*;
import lombok.experimental.Accessors;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EndpointHitDto {
    public static final String DATA_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private Integer id;

    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}