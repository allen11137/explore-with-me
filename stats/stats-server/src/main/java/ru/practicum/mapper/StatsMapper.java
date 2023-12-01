package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.EndpointHitDto;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StatsMapper {
    public static final String DATA_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public Stats toStats(EndpointHitDto endpointHitDto) {
        return new Stats()
                .setApp(endpointHitDto.getApp())
                .setId(endpointHitDto.getId())
                .setIp(endpointHitDto.getIp())
                .setTimestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)))
                .setUri(endpointHitDto.getUri());
    }
}