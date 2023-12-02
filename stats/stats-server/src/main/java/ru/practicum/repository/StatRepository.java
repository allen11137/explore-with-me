package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.StatsDto;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Stats, Integer> {


    @Query(value = "select new ru.practicum.statsDto.ViewStats(eh.app, eh.uri, cast(count(eh.ip) " +
            "AS int) as hits) " +
            "from EndpointHit as eh " +
            "where eh.uri in ?3 " +
            "and eh.timestamp >= ?1 " +
            "and eh.timestamp <=?2 " +
            "group by eh.app, eh.uri order by hits desc")
    List<Stats> findStats(LocalDateTime startTime, LocalDateTime endTime, Collection<String> uris);

    @Query(value = "select new ru.practicum.statsDto.ViewStats(eh.app, eh.uri, cast(count(eh.ip) " +
            "AS int) as hits) " +
            "from EndpointHit as eh " +
            "where eh.timestamp >= ?1 " +
            "and eh.timestamp <=?2 " +
            "group by eh.app, eh.uri order by hits desc")
    List<Stats> findAllStats(LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = "select new ru.practicum.statsDto.ViewStats(eh.app, eh.uri, cast(count(DISTINCT eh.ip) " +
            "AS int) as hits) " +
            "from EndpointHit as eh " +
            "where eh.uri in ?3 " +
            "and eh.timestamp >= ?1 " +
            "and eh.timestamp <=?2 " +
            "group by eh.app, eh.uri order by hits desc")
    List<Stats> findUniqueStats(LocalDateTime startTime, LocalDateTime endTime, Collection<String> uris);

    @Query(value = "select new ru.practicum.statsDto.ViewStats(eh.app, eh.uri,cast(count(DISTINCT eh.ip) " +
            "AS int) as hits) " +
            "from EndpointHit as eh " +
            "where eh.timestamp >= ?1 " +
            "and eh.timestamp <=?2 " +
            "group by eh.app, eh.uri order by hits desc")
    List<Stats> findAllUniqueStats(LocalDateTime startTime, LocalDateTime endTime);
}