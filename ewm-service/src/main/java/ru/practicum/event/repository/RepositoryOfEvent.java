package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface RepositoryOfEvent extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    List<Event> getEventsByStateIn(List<State> states, Pageable pageable);

    List<Event> getEventsByInitiatorIdIn(List<Long> users, Pageable pageable);

    List<Event> getEventsByCategoryIdInAndStateIn(List<Long> categories, List<State> states, Pageable pageable);

    List<Event> getEventsByCategoryIdIn(List<Long> categories, Pageable pageable);

    List<Event> getEventsByInitiatorIdInAndCategoryIdIn(List<Long> users, List<Long> categories, Pageable pageable);

    List<Event> getEventsByInitiatorIdInAndStateIn(List<Long> users, List<State> states, Pageable pageable);

    List<Event> getEventsByEventDateAfterAndEventDateBefore(LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Event> getEventsByInitiatorIdInAndStateInAndCategoryIdIn(List<Long> users, List<State> states, List<Long> categories, Pageable pageable);

    Event getEventByIdAndState(Long eventId, State state);

    List<Event> getEventsByStateInAndEventDateAfterAndEventDateBefore(List<State> states, LocalDateTime start, LocalDateTime end,
                                                                      Pageable pageable);

    List<Event> getEventsByCategoryIdInAndEventDateAfterAndEventDateBefore(List<Long> categories, LocalDateTime start,
                                                                           LocalDateTime end, Pageable pageable);

    List<Event> getEventsByStateInAndCategoryIdInAndEventDateAfterAndEventDateBefore(List<State> states, List<Long> categories,
                                                                                     LocalDateTime start, LocalDateTime end,
                                                                                     Pageable pageable);

    List<Event> getEventsByInitiatorIdInAndEventDateAfterAndEventDateBefore(List<Long> users, LocalDateTime start, LocalDateTime end,
                                                                            Pageable pageable);


    List<Event> getEventsByInitiatorIdInAndCategoryIdInAndEventDateAfterAndEventDateBefore(List<Long> users, List<Long> categories,
                                                                                           LocalDateTime start, LocalDateTime end,
                                                                                           Pageable pageable);

    List<Event> getEventsByInitiatorIdInAndStateInAndEventDateAfterAndEventDateBefore(List<Long> users, List<State> states,
                                                                                      LocalDateTime start, LocalDateTime end,
                                                                                      Pageable pageable);

    List<Event> getEventsByInitiatorIdInAndStateInAndCategoryIdInAndEventDateAfterAndEventDateBefore(List<Long> users,
                                                                                                     List<State> stateEnum,
                                                                                                     List<Long> categories,
                                                                                                     LocalDateTime start,
                                                                                                     LocalDateTime end,
                                                                                                     Pageable pageable);

    List<Event> getEventsByInitiatorId(Long userId, Pageable pageable);

    Optional<Event> getEventsByIdAndInitiatorId(Long eventId, Long userId);

    Event findFirstByCategoryId(Long catId);

    Set<Event> getEventsByIdIn(List<Long> events);

    Optional<Event> findById(Long id);

    List<Event> getEventsByInitiatorId(Long userId);

    @Query(value = "select * from events as e " +
            "where e.state = :state " +
            "and e.category_id in :category " +
            "and e.paid = :paid " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > :time " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(@Param("state") String state,
                          @Param("category") List<Long> category,
                          @Param("paid") boolean paid,
                          @Param("time") LocalDateTime time,
                          Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state = :state " +
            "and e.category_id in :category " +
            "and e.paid = :paid " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > :time " +
            "and upper(e.annotation) like upper(:text) or upper(e.description) like upper(:text) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(@Param("state") String state,
                          @Param("category") List<Long> category,
                          @Param("paid") boolean paid,
                          @Param("time") LocalDateTime time,
                          @Param("text") String text,
                          Pageable pageable);


    @Query(value = "select * from events as e " +
            "where e.state = :state " +
            "and e.paid= :paid " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > :time " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(@Param("state") String state,
                          @Param("paid") boolean paid,
                          @Param("time") LocalDateTime time,
                          Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state = :state " +
            "and e.paid= :paid " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > :time " +
            "and upper(e.annotation) like upper(:text) or upper(e.description) like upper(:text) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(@Param("state") String state,
                          @Param("paid") boolean paid,
                          @Param("time") LocalDateTime time,
                          @Param("text") String text,
                          Pageable pageable);


    @Query(value = "select * from events as e " +
            "where e.state = :state " +
            "and e.category_id in :category " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > :time " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(@Param("state") String state,
                          @Param("category") List<Long> category,
                          @Param("time") LocalDateTime time,
                          Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state = :state " +
            "and e.category_id in :category " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > :time " +
            "and upper(e.annotation) like upper(:text) or upper(e.description) like upper(:text) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(@Param("state") String state,
                          @Param("category") List<Long> category,
                          @Param("time") LocalDateTime time,
                          @Param("text") String text,
                          Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state = :state " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > :time " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(@Param("state") String state,
                          @Param("time") LocalDateTime time,
                          Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state = :state " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > :time " +
            "and upper(e.annotation) like upper(:text) or upper(e.description) like upper(:text) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(@Param("state") String state,
                          @Param("time") LocalDateTime time,
                          @Param("text") String text,
                          Pageable pageable);


    @Query(value = "select * from events as e " +
            "where e.state = :state " +
            "and e.category_id in :category " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= :timeStart and e.event_date <= :timeEnd " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(@Param("state") String state,
                          @Param("category") List<Long> category,
                          @Param("timeStart") LocalDateTime timeStart,
                          @Param("timeEnd") LocalDateTime timeEnd,
                          Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state = :state " +
            "and e.category_id in :category " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= :timeStart and e.event_date <= :timeEnd " +
            "and upper(e.annotation) like upper(:text) or upper(e.description) like upper(:text) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(@Param("state") String state,
                          @Param("category") List<Long> category,
                          @Param("timeStart") LocalDateTime timeStart,
                          @Param("timeEnd") LocalDateTime timeEnd,
                          @Param("text") String text,
                          Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state = :state " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= :timeStart and e.event_date <= :timeEnd " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(@Param("state") String state,
                          @Param("timeStart") LocalDateTime timeStart,
                          @Param("timeEnd") LocalDateTime timeEnd,
                          Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state = :state " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= :timeStart and e.event_date <= :timeEnd " +
            "and upper(e.annotation) like upper(:text) or upper(e.description) like upper(:text) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(@Param("state") String state,
                          @Param("timeStart") LocalDateTime timeStart,
                          @Param("timeEnd") LocalDateTime timeEnd,
                          @Param("text") String text,
                          Pageable pageable);

}