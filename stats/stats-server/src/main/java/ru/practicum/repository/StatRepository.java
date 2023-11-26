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

    @Query("SELECT NEW ru.practicum.StatsDto(h.app, h.uri, COUNT(DISTINCT(h.ip))) " +
            "FROM Stats h " +
            "WHERE h.uri IN :uris " +
            "AND (h.timestamp BETWEEN :start AND :end) " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(DISTINCT(h.ip)) DESC")
    List<StatsDto> getUniqueStatsByUrisAndBetweenStart(
            LocalDateTime start,
            LocalDateTime end,
            Collection<String> uris
    );

    @Query("SELECT NEW ru.practicum.StatsDto(h.app, h.uri, COUNT(DISTINCT(h.ip))) " +
            "FROM Stats h " +
            "WHERE (h.timestamp BETWEEN :start AND :end) " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(DISTINCT(h.ip)) DESC")
    List<StatsDto> getUniqueStatsBetweenStartAndEnd(
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("SELECT NEW ru.practicum.StatsDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM Stats h " +
            "WHERE h.uri IN :uris " +
            "AND (h.timestamp BETWEEN :start AND :end) " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(h.ip) DESC")
    List<StatsDto> getStatsByUri(
            LocalDateTime start,
            LocalDateTime end,
            Collection<String> uris
    );

    @Query("SELECT NEW ru.practicum.StatsDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM Stats h " +
            "WHERE (h.timestamp BETWEEN :start AND :end) " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(h.ip) DESC")
    List<StatsDto> getStatsBetweenStartAndEnd(
            LocalDateTime start,
            LocalDateTime end
    );
}