package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@UtilityClass
public class MapperOfStats {
    public static final String DATA_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public EndpointHitDto toEndpointHitDto(Stats stats) {
        return new EndpointHitDto()
                .setId(stats.getId())
                .setApp(stats.getApp())
                .setUri(stats.getUri())
                .setIp(stats.getIp())
                .setTimestamp(stats.getTimestamp().format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)));
    }

    public Stats toStats(EndpointHitDto endpointHitDto) {
        return new Stats()
                .setApp(endpointHitDto.getApp())
                .setUri(endpointHitDto.getUri())
                .setIp(endpointHitDto.getIp())
                .setTimestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)));
    }
}