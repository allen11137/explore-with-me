package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import static ru.practicum.EndpointHitDto.DATA_TIME_PATTERN;

@Service
@RequiredArgsConstructor
public class StatService {
    private final StatRepository statRepository;
    private final StatsMapper statsMapper;

    public Stats createStatHit(EndpointHitDto endpointHitDto) {
        return statRepository.save(statsMapper.toStats(endpointHitDto));
    }

    public List<Stats> getStatHit(LocalDateTime start, LocalDateTime end, Collection<String> uris, boolean isUnique) {
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