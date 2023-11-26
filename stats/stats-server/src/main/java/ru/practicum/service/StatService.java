package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsDto;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatService {
    private final StatRepository statRepository;
    private final StatsMapper statsMapper;

    public Stats createStatHit(EndpointHitDto endpointHitDto) {
        return statRepository.save(statsMapper.toStats(endpointHitDto));
    }

    public List<StatsDto> getStatHit(LocalDateTime start, LocalDateTime end, Collection<String> uris, boolean isUnique) {
        if (uris != null && !uris.isEmpty()) {
            if (isUnique) {
                return statRepository.getUniqueStatsByUrisAndBetweenStart(start, end, uris);
            } else {
                return statRepository.getStatsByUri(start, end, uris);
            }
        } else {
            if (isUnique) {
                return statRepository.getUniqueStatsBetweenStartAndEnd(start, end);
            } else {
                return statRepository.getStatsBetweenStartAndEnd(start, end);
            }
        }
    }
}