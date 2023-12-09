package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.StatsView;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface RepositoryOfStats extends JpaRepository<Stats, Integer> {

    @Query(value = "select new ru.practicum.dto.StatsView(s.app, s.uri, cast(count(s.ip) " +
            "AS int) as hits) " +
            "from Stats as s " +
            "where s.uri in :uris " +
            "and s.timestamp >= :startTime " +
            "and s.timestamp <= :endTime " +
            "group by s.app, s.uri order by hits desc")
    List<StatsView> findStats(@Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime,
                              @Param("uris") Collection<String> uris);

    @Query(value = "select new ru.practicum.dto.StatsView(s.app, s.uri, cast(count(s.ip) " +
            "AS int) as hits) " +
            "from Stats as s " +
            "where s.timestamp >= :startTime " +
            "and s.timestamp <= :endTime " +
            "group by s.app, s.uri order by hits desc")
    List<StatsView> findAllStats(@Param("startTime") LocalDateTime startTime,
                                 @Param("endTime") LocalDateTime endTime);

    @Query(value = "select new ru.practicum.dto.StatsView(s.app, s.uri, cast(count(DISTINCT s.ip) " +
            "AS int) as hits) " +
            "from Stats as s " +
            "where s.uri in :uris " +
            "and s.timestamp >= :startTime " +
            "and s.timestamp <= :endTime " +
            "group by s.app, s.uri order by hits desc")
    List<StatsView> findUniqueStats(@Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime,
                                    @Param("uris") Collection<String> uris);

    @Query(value = "select new ru.practicum.dto.StatsView(s.app, s.uri, cast(count(DISTINCT s.ip) " +
            "AS int) as hits) " +
            "from Stats as s " +
            "where s.timestamp >= :startTime " +
            "and s.timestamp <= :endTime " +
            "group by s.app, s.uri order by hits desc")
    List<StatsView> findAllUniqueStats(@Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime);
}