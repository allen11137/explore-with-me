package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsView;
import ru.practicum.mapper.MapperOfStats;
import ru.practicum.repository.RepositoryOfStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import static ru.practicum.controller.StatController.DATA_TIME_PATTERN;

@Service
@RequiredArgsConstructor
public class StatService {
    private final RepositoryOfStats repositoryOfStats;

    public EndpointHitDto createStatHit(EndpointHitDto endpointHitDto) {
        return MapperOfStats.toEndpointHitDto(repositoryOfStats.save(MapperOfStats.toStats(endpointHitDto)));
    }

    public List<StatsView> getListOfStatHit(LocalDateTime start, LocalDateTime end, Collection<String> uris, boolean isUnique) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException(String.format("Время окончания %s не может быть раньше времени начала %s",
                    end.format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)),
                    start.format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN))));
        }

        if (!isUnique) {
            if (uris == null) {
                return repositoryOfStats.findAllStats(start, end);
            } else {
                return repositoryOfStats.findStats(start, end, uris);
            }
        } else {
            if (uris == null) {
                return repositoryOfStats.findAllUniqueStats(start, end);
            } else {
                return repositoryOfStats.findUniqueStats(start, end, uris);
            }
        }
    }
}