package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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

    Event getEventsById(Long eventId);


    List<Event> getEventsByInitiatorId(Long userId);


    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?4 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(String state, List<Long> category, boolean paid, LocalDateTime time, Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(String state, List<Long> category, boolean paid, LocalDateTime time, String text, Pageable pageable);


    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?3 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(String state, boolean paid, LocalDateTime time, Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(String state, boolean paid, LocalDateTime time, String text, Pageable pageable);


    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?3 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(String state, List<Long> category, LocalDateTime time, Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(String state, List<Long> category, LocalDateTime time, String text, Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?2 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(String state, LocalDateTime time, Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?2 " +
            "and upper(e.annotation) like upper(?3) or upper(e.description) like upper(?3) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(String state, LocalDateTime time, String text, Pageable pageable);


    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(String state, List<Long> category, LocalDateTime timeStart, LocalDateTime timeEnd, Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(String state, List<Long> category, LocalDateTime timeStart, LocalDateTime timeEnd, String text,
                          Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >=  ?2 and e.event_date <= ?3 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(String state, LocalDateTime timeStart, LocalDateTime timeEnd, Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?2 and e.event_date <= ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEvents(String state, LocalDateTime timeStart, LocalDateTime timeEnd, String text, Pageable pageable);

}