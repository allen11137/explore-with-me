package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class StatsMapper {
    public static final String DATA_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static EndpointHitDto toEndpointHitDto(Stats stats) {
        return new EndpointHitDto()
                .setId(stats.getId())
                .setApp(stats.getApp())
                .setUri(stats.getUri())
                .setIp(stats.getIp())
                .setTimestamp(stats.getTimestamp().format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)));
    }

    public static Stats toStats(EndpointHitDto endpointHitDto) {
        return new Stats()
                .setApp(endpointHitDto.getApp())
                .setId(endpointHitDto.getId())
                .setIp(endpointHitDto.getIp())
                .setTimestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)))
                .setUri(endpointHitDto.getUri());
    }
}