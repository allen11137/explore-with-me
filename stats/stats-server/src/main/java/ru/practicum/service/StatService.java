package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsView;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import static ru.practicum.dto.EndpointHitDto.DATA_TIME_PATTERN;

@Service
@RequiredArgsConstructor
public class StatService {
    private final StatRepository statRepository;

    public EndpointHitDto createStatHit(EndpointHitDto endpointHitDto) {
        return StatsMapper.toEndpointHitDto(statRepository.save(StatsMapper.toStats(endpointHitDto)));
    }

    public List<StatsView> getStatHit(LocalDateTime start, LocalDateTime end, Collection<String> uris, boolean isUnique) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException(String.format("Time end %s can not be after start %s",
                    end.format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)),
                    start.format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN))));
        }

        if (!isUnique) {
            if (uris == null) {
                return statRepository.findAllStats(start, end);
            } else {
                return statRepository.findStats(start, end, uris);
            }
        } else {
            if (uris == null) {
                return statRepository.findAllUniqueStats(start, end);
            } else {
                return statRepository.findUniqueStats(start, end, uris);
            }
        }
    }
}