package ru.practicum.event.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.StatsDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.RepositoryOfCategory;
import ru.practicum.client.Client;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.MapperOfEvent;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.model.Status;
import ru.practicum.event.repository.RepositoryOfEvent;
import ru.practicum.exception.*;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.RepositoryOfLocation;
import ru.practicum.participant.dto.ParticipantRequestDto;
import ru.practicum.participant.mapper.MapperOfParticipant;
import ru.practicum.participant.model.Participant;
import ru.practicum.participant.repository.RepositoryOfParticipant;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import static ru.practicum.event.Constant.DATA_TIME_PATTERN;

@Slf4j
@Service
@AllArgsConstructor
public class EventService {
    private final RepositoryOfEvent eventRepository;
    private final RepositoryOfCategory repositoryOfCategory;
    private final UserService userService;
    private final RepositoryOfParticipant participationRepository;
    private final RepositoryOfLocation locationRepository;
    private final Client client;

    @Transactional
    public List<EventBriefDto> getPublicEventsAndStats(HttpServletRequest request, String text, List<Long> categories,
                                                       Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                       Boolean onlyAvailable, String sort, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        LocalDateTime timeNow = LocalDateTime.now();

        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new IllegalArgumentException("Range start time is after range end time");
            }
        }

        List<Event> list = new ArrayList<>();
        if (text != null) {
            text = "%" + text + "%";
        }
        if (paid != null) {
            if (categories != null && text != null) {
                list = eventRepository.getEvents(
                        State.PUBLISHED.toString(), categories, paid, timeNow, text, pageable);

            } else if (text == null && categories != null) {
                list = eventRepository.getEvents(
                        State.PUBLISHED.toString(), categories, paid, timeNow, pageable);

            } else if (text != null) {
                list = eventRepository.getEvents(
                        State.PUBLISHED.toString(), paid, timeNow, text, pageable);
            } else {
                list = eventRepository.getEvents(
                        State.PUBLISHED.toString(), paid, timeNow, pageable);
            }

        } else {
            if (rangeStart == null && rangeEnd == null) {
                if (categories != null && text != null) {
                    list = eventRepository.getEvents(
                            State.PUBLISHED.toString(), categories, timeNow, text, pageable);
                } else if (text == null && categories != null) {
                    list = eventRepository.getEvents(State.PUBLISHED.toString(),
                            categories, timeNow, pageable);
                } else if (text != null) {
                    list = eventRepository.getEvents(State.PUBLISHED.toString(),
                            timeNow, text, pageable);
                } else {
                    list = eventRepository.getEvents(State.PUBLISHED.toString(),
                            timeNow, pageable);
                }
            } else {
                if (sort != null && sort.equals("EVENT_DATE")) {
                    list = eventRepository.getEvents(State.PUBLISHED.toString(),
                            categories, rangeStart, rangeEnd, text, pageable);
                } else if (text == null && categories != null) {
                    list = eventRepository.getEvents(State.PUBLISHED.toString(),
                            categories, rangeStart, rangeEnd, pageable);

                } else if (text != null) {
                    list = eventRepository.getEvents(State.PUBLISHED.toString(), rangeStart,
                            rangeEnd, text, pageable);
                } else {
                    list = eventRepository.getEvents(State.PUBLISHED.toString(),
                            rangeStart, rangeEnd, pageable);
                }
            }
        }

        EndpointHitDto endpointHitDto = new EndpointHitDto(null, "main-service", request.getRequestURI(),
                request.getRemoteAddr(), timeNow.format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)));

        try {
            client.addRequest(request.getRemoteAddr(), endpointHitDto);
        } catch (
                RuntimeException e) {
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }

        if (list.isEmpty()) {
            return new ArrayList<>();
        }

        return list.stream().map(MapperOfEvent::toEventShortDto).collect(Collectors.toList());
    }

    @Transactional
    public EventCompleteDto getPublicEventByIdAndStats(HttpServletRequest request, Long eventId) {
        Event event = eventRepository.getEventByIdAndState(eventId, State.PUBLISHED);
        if (event == null) {
            throw new NotFoundException("Событие не найдено.");
        }
        String timeStart = event.getCreatedOn().format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN));
        String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN));
        String[] uris = {request.getRequestURI()};

        ResponseEntity<Object> response = client.getStat(request.getRequestURI(), timeStart, timeNow, uris, true);
        List<StatsDto> resp = (List<StatsDto>) response.getBody();
        if (resp.size() == 0) {
            event.setViews(event.getViews() + 1);
            eventRepository.save(event);
        }

        EndpointHitDto endpointHitDto = new EndpointHitDto(null,
                "main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                timeNow);

        client.addRequest(request.getRemoteAddr(), endpointHitDto);

        return MapperOfEvent.toEventFullDto(event);
    }

    @Transactional
    public List<EventCompleteDto> getAdminEvents(List<Long> users, List<String> states, List<Long> categories,
                                                 String rangeStart, String rangeEnd, Integer from, Integer size) {

        List<EventCompleteDto> list;
        List<State> stateEnum = null;
        if (states != null) {
            stateEnum = states.stream().map(State::valueOf).collect(Collectors.toList());
        }
        Pageable pageable = PageRequest.of(from / size, size);
        if (rangeStart == null && rangeEnd == null) {
            if (users == null && states == null && categories == null) {
                list = eventRepository.findAll(pageable).stream()
                        .map(MapperOfEvent::toEventFullDto)
                        .collect(Collectors.toList());
            } else if (users == null && states == null) {
                list = eventRepository.getEventsByCategoryIdIn(categories, pageable).stream()
                        .map(MapperOfEvent::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (users == null && categories == null) {
                list = eventRepository.getEventsByStateIn(stateEnum, pageable).stream()
                        .map(MapperOfEvent::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (users != null && states == null && categories == null) {
                list = eventRepository.getEventsByInitiatorIdIn(users, pageable).stream()
                        .map(MapperOfEvent::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (users == null) {
                list = eventRepository.getEventsByCategoryIdInAndStateIn(categories, stateEnum, pageable).stream()
                        .map(MapperOfEvent::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (states != null && categories == null) {
                list = eventRepository.getEventsByInitiatorIdInAndStateIn(users, stateEnum, pageable).stream()
                        .map(MapperOfEvent::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (states == null) {
                list = eventRepository.getEventsByInitiatorIdInAndCategoryIdIn(users, categories, pageable).stream()
                        .map(MapperOfEvent::toEventFullDto)
                        .collect(Collectors.toList());

            } else {
                list = eventRepository.getEventsByInitiatorIdInAndStateInAndCategoryIdIn(users, stateEnum, categories, pageable).stream()
                        .map(MapperOfEvent::toEventFullDto)
                        .collect(Collectors.toList());
            }
        } else {
            LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(DATA_TIME_PATTERN));
            LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(DATA_TIME_PATTERN));
            if (start.isAfter(end)) {
                throw new IllegalArgumentException();
            }
            if (users == null && states == null && categories == null) {
                list = eventRepository.getEventsByEventDateAfterAndEventDateBefore(start, end, pageable).stream()
                        .map(MapperOfEvent::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (users == null && states == null) {
                list = eventRepository.getEventsByCategoryIdInAndEventDateAfterAndEventDateBefore(
                                categories, start, end, pageable).stream()
                        .map(MapperOfEvent::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (users == null && categories == null) {
                list = eventRepository.getEventsByStateInAndEventDateAfterAndEventDateBefore(
                                stateEnum, start, end, pageable).stream()
                        .map(MapperOfEvent::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (users != null && states == null && categories == null) {
                list = eventRepository.getEventsByInitiatorIdInAndEventDateAfterAndEventDateBefore(
                                users, start, end, pageable).stream()
                        .map(MapperOfEvent::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (users == null) {
                list = eventRepository.getEventsByStateInAndCategoryIdInAndEventDateAfterAndEventDateBefore(
                                stateEnum, categories, start, end, pageable).stream()
                        .map(MapperOfEvent::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (states != null && categories == null) {
                list = eventRepository.getEventsByInitiatorIdInAndStateInAndEventDateAfterAndEventDateBefore(
                                users, stateEnum, start, end, pageable).stream()
                        .map(MapperOfEvent::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (states == null) {
                list = eventRepository.getEventsByInitiatorIdInAndCategoryIdInAndEventDateAfterAndEventDateBefore(
                                users, categories, start, end, pageable).stream()
                        .map(MapperOfEvent::toEventFullDto)
                        .collect(Collectors.toList());

            } else
                list = eventRepository.getEventsByInitiatorIdInAndStateInAndCategoryIdInAndEventDateAfterAndEventDateBefore(
                                users, stateEnum, categories, start, end, pageable)
                        .stream()
                        .map(MapperOfEvent::toEventFullDto)
                        .collect(Collectors.toList());
        }

        return list;
    }

    @Transactional
    public EventCompleteDto updateAdminEvent(Long eventId, AdminUpdateEventRequest updateEventAdminRequest) {
        Event oldEvent = eventRepository.getEventsById(eventId);

        validateAdminUpdateEvent(oldEvent, updateEventAdminRequest);

        if (updateEventAdminRequest.getLocation() != null) {
            Location location = locationRepository.save(updateEventAdminRequest.getLocation());
            updateEventAdminRequest.setLocation(location);
        }

        Category newCategory = updateEventAdminRequest.getCategory() == null ?
                oldEvent.getCategory() : repositoryOfCategory.getById(updateEventAdminRequest.getCategory());

        Event upEvent = oldEvent;
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals("PUBLISH_EVENT")) {
                upEvent = MapperOfEvent.toEvent(updateEventAdminRequest, oldEvent, newCategory);
                upEvent.setPublishedOn(LocalDateTime.now());
                upEvent.setState(State.PUBLISHED);
            }
            if (updateEventAdminRequest.getStateAction().equals("REJECT_EVENT")) {
                upEvent.setState(State.CANCELED);

            }
        }
        upEvent.setId(eventId);

        return MapperOfEvent.toEventFullDto(eventRepository.save(upEvent));
    }


    private void validateAdminUpdateEvent(Event oldEvent, AdminUpdateEventRequest updateEventAdminRequest) {
        if (oldEvent == null) {
            throw new NotFoundException("Событие не найдено.");
        }

        LocalDateTime start = oldEvent.getEventDate();
        if (oldEvent.getPublishedOn() != null && start.isAfter(oldEvent.getPublishedOn().plusHours(1))) {
            throw new DateEventException("Время начала до даты мероприятия");
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            LocalDateTime newEventDate = LocalDateTime.parse(updateEventAdminRequest.getEventDate(), DateTimeFormatter.ofPattern(DATA_TIME_PATTERN));
            LocalDateTime currentTime = LocalDateTime.now();
            if (newEventDate.isBefore(currentTime) || newEventDate.isEqual(currentTime)) {
                throw new IllegalArgumentException("Время начала раньше или равно дате события");
            }
        }

        if (oldEvent.getState() != null && !oldEvent.getState().equals(State.PENDING) && updateEventAdminRequest.getStateAction().equals("PUBLISH_EVENT")) {
            throw new ArgumentStateException("Неправильный статус: ОТМЕНЕНО ИЛИ ОПУБЛИКОВАНО");
        }
        if (oldEvent.getState() != null && oldEvent.getState().equals(State.PUBLISHED) && updateEventAdminRequest.getStateAction().equals("REJECT_EVENT")) {
            throw new ArgumentStateException("Неправильный статус: ОПУБЛИКОВАНО");
        }
    }

    @Transactional
    public List<EventBriefDto> getPrivateEvents(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return eventRepository.getEventsByInitiatorId(userId, pageable)
                .stream()
                .map(MapperOfEvent::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventCompleteDto addPrivateEvent(Long userId, AddEventDto newEventDto) {
        LocalDateTime start = LocalDateTime.parse(newEventDto.getEventDate(),
                DateTimeFormatter.ofPattern(DATA_TIME_PATTERN));

        if (start.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IllegalArgumentException("Время начала указано неверно.");
        }
        Location location = locationRepository.save(newEventDto.getLocation());
        newEventDto.setLocation(location);
        Category category = repositoryOfCategory.getById(newEventDto.getCategory());
        User user = userService.getUserById(userId);

        Event event = MapperOfEvent.toEvent(newEventDto, user, category);
        return MapperOfEvent.toEventFullDto(eventRepository.save(event));
    }

    @Transactional
    public EventCompleteDto getPrivateEvent(Long userId, Long eventId) {
        return MapperOfEvent.toEventFullDto(getEventById(userId, eventId));
    }

    public Event getEventById(Long userId, Long eventId) {
        return eventRepository.getEventsByIdAndInitiatorId(eventId, userId).orElseThrow(() -> {
            throw new NotFoundException("Событие не найдено.");
        });
    }


    @Transactional
    public EventCompleteDto updatePrivateEvent(Long userId, Long eventId, UserUpdateEventRequest updateEventUserRequest) {
        Event oldEvent = getEventById(userId, eventId);
        validatePrivateUpdateEvent(oldEvent, updateEventUserRequest);
        Category newCategory = getCategory(updateEventUserRequest, oldEvent);
        Event upEvent = oldEvent;
        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals("SEND_TO_REVIEW")) {
                upEvent = MapperOfEvent.toEvent(updateEventUserRequest, oldEvent, newCategory);
                upEvent.setState(State.PENDING);
            }
            if (updateEventUserRequest.getStateAction().equals("CANCEL_REVIEW")) {
                upEvent.setState(State.CANCELED);
            }
        }
        upEvent.setId(eventId);
        return MapperOfEvent.toEventFullDto(eventRepository.save(upEvent));
    }

    private void validatePrivateUpdateEvent(Event oldEvent, UserUpdateEventRequest updateEventUserRequest) {
        if (oldEvent.getState() != null && oldEvent.getState().equals(State.PUBLISHED)) {
            throw new ArgumentStateException("Невозможно отменить события, которые не ожидаются или не отменены.");
        }
        LocalDateTime start = oldEvent.getEventDate();
        if (updateEventUserRequest.getEventDate() != null) {
            if (LocalDateTime.parse(updateEventUserRequest.getEventDate(), DateTimeFormatter.ofPattern(DATA_TIME_PATTERN))
                    .isBefore(start.plusHours(2))) {
                throw new IllegalArgumentException("Время начала предшествует событию или равно ему.");
            }
        }
    }

    private Category getCategory(UserUpdateEventRequest updateEventUserRequest, Event oldEvent) {
        if (updateEventUserRequest.getLocation() != null) {
            Location location = locationRepository.save(updateEventUserRequest.getLocation());
            updateEventUserRequest.setLocation(location);
        }
        return updateEventUserRequest.getCategory() == null ?
                oldEvent.getCategory() : repositoryOfCategory.getById(updateEventUserRequest.getCategory());
    }


    @Transactional
    public List<ParticipantRequestDto> getPrivateRequestsEventsUser(Long userId, Long eventId) {
        return participationRepository.getParticipantRequestsByEvent(eventId)
                .stream()
                .map(MapperOfParticipant::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventStatusUpdateResponse updatePrivateEventRequestStatus(Long userId,
                                                                     Long eventId,
                                                                     EventStatusUpdateRequest eventRequestStatusUpdateRequest) {
        Event event = getEventById(userId, eventId);
        if (Long.valueOf(event.getParticipantLimit()).equals(event.getConfirmedRequests())) {
            throw new OverflowLimitException("Превышено количество участников.");
        }
        Status status = Status.valueOf(eventRequestStatusUpdateRequest.getStatus());

        List<Participant> list = participationRepository.getParticipantRequestByIdIn(eventRequestStatusUpdateRequest.getRequestIds());
        List<Participant> listPending = new ArrayList<>();
        List<Participant> listRejected = new ArrayList<>();
        List<Participant> listOld = new ArrayList<>();
        List<ParticipantRequestDto> listDto = new ArrayList<>();
        List<ParticipantRequestDto> listDtoReject = new ArrayList<>();

        if (event.getParticipantLimit() == 0 && Boolean.TRUE.equals(!event.getRequestModeration())) {
            return new EventStatusUpdateResponse(listDto, listDtoReject);
        } else if (event.getParticipantLimit() > 0 && Boolean.TRUE.equals(!event.getRequestModeration())) {
            for (Participant participationRequest : list) {
                if (!participationRequest.getStatus().equals(Status.PENDING)) {
                    throw new ParticipantStatusException("Неправильный запрос статуса.");
                }
                EventStatusUpdateResponse listDto1 = getEventRequestStatusUpdateResult(status, listOld, participationRequest, listPending, event, list, listDto, listDtoReject, listRejected);
                if (listDto1 != null) return listDto1;
            }
            listDto = listPending.stream().map(MapperOfParticipant::toParticipationRequestDto).collect(Collectors.toList());
            return new EventStatusUpdateResponse(listDto, new ArrayList<>());
        } else if (event.getParticipantLimit() > 0 && Boolean.TRUE.equals(event.getRequestModeration())) {
            for (Participant participationRequest : list) {
                if (!participationRequest.getStatus().equals(Status.PENDING)) {
                    throw new ParticipantStatusException("Неправильный запрос статуса.");
                }
                EventStatusUpdateResponse listDto1 = getEventRequestStatusUpdateResult(status, listOld, participationRequest, listPending, event, list, listDto, listDtoReject, listRejected);
                if (listDto1 != null) return listDto1;
            }
        }
        listDto = listPending.stream().map(MapperOfParticipant::toParticipationRequestDto).collect(Collectors.toList());
        return new EventStatusUpdateResponse(listDto, new ArrayList<>());

    }

    private EventStatusUpdateResponse getEventRequestStatusUpdateResult(Status status, List<Participant> listOld,
                                                                        Participant participationRequest,
                                                                        List<Participant> listPending, Event event,
                                                                        List<Participant> list,
                                                                        List<ParticipantRequestDto> listDto,
                                                                        List<ParticipantRequestDto> listDtoReject,
                                                                        List<Participant> listRejected) {

        if (status.equals(Status.CONFIRMED)) {
            listOld.add(participationRequest);
            participationRequest.setStatus(Status.CONFIRMED);
            listPending.add(participationRequest);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            participationRepository.saveAndFlush(participationRequest);

            if (Long.valueOf(event.getParticipantLimit()).equals(event.getConfirmedRequests())) {
                list.removeAll(listOld);
                if (list.size() != 0) {
                    listDto = listPending.stream().map(MapperOfParticipant::toParticipationRequestDto).collect(Collectors.toList());
                    listDtoReject = list.stream().map(MapperOfParticipant::toParticipationRequestDto).collect(Collectors.toList());
                    return new EventStatusUpdateResponse(listDto, listDtoReject);
                } else {
                    listDto = listPending.stream().map(MapperOfParticipant::toParticipationRequestDto).collect(Collectors.toList());
                    return new EventStatusUpdateResponse(listDto, new ArrayList<>());
                }
            }
        } else {
            participationRequest.setStatus(Status.REJECTED);
            listRejected.add(participationRequest);
            participationRepository.saveAndFlush(participationRequest);
            listDtoReject = list.stream().map(MapperOfParticipant::toParticipationRequestDto).collect(Collectors.toList());
            return new EventStatusUpdateResponse(new ArrayList<>(), listDtoReject);
        }
        return null;
    }

}