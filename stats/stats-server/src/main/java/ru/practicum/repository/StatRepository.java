package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.StatsView;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Stats, Integer> {

    @Query(value = "select new ru.practicum.dto.StatsView(s.app, s.uri, cast(count(s.ip) " +
            "AS int) as hits) " +
            "from Stats as s " +
            "where s.uri in ?3 " +
            "and s.timestamp >= ?1 " +
            "and s.timestamp <=?2 " +
            "group by s.app, s.uri order by hits desc")
    List<StatsView> findStats(LocalDateTime startTime, LocalDateTime endTime, Collection<String> uris);

    @Query(value = "select new ru.practicum.dto.StatsView(s.app, s.uri, cast(count(s.ip) " +
            "AS int) as hits) " +
            "from Stats as s " +
            "where s.timestamp >= ?1 " +
            "and s.timestamp <=?2 " +
            "group by s.app, s.uri order by hits desc")
    List<StatsView> findAllStats(LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = "select new ru.practicum.dto.StatsView(s.app, s.uri, cast(count(DISTINCT s.ip) " +
            "AS int) as hits) " +
            "from Stats as s " +
            "where s.uri in ?3 " +
            "and s.timestamp >= ?1 " +
            "and s.timestamp <=?2 " +
            "group by s.app, s.uri order by hits desc")
    List<StatsView> findUniqueStats(LocalDateTime startTime, LocalDateTime endTime, Collection<String> uris);

    @Query(value = "select new ru.practicum.dto.StatsView(s.app, s.uri,cast(count(DISTINCT s.ip) " +
            "AS int) as hits) " +
            "from Stats as s " +
            "where s.timestamp >= ?1 " +
            "and s.timestamp <=?2 " +
            "group by s.app, s.uri order by hits desc")
    List<StatsView> findAllUniqueStats(LocalDateTime startTime, LocalDateTime endTime);
}