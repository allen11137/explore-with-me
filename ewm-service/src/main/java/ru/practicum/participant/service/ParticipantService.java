package ru.practicum.participant.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.model.Status;
import ru.practicum.event.repository.RepositoryOfEvent;
import ru.practicum.exception.DoubleParticipationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.OverflowLimitException;
import ru.practicum.participant.dto.ParticipantRequestDto;
import ru.practicum.participant.mapper.MapperOfParticipant;
import ru.practicum.participant.model.Participant;
import ru.practicum.participant.repository.RepositoryOfParticipant;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class ParticipantService {
    private final RepositoryOfParticipant participationRepository;
    private final RepositoryOfEvent eventRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<ParticipantRequestDto> getListOfParticipationRequest(Long userId) {
        userService.getUserById(userId);
        List<Long> eventIds = eventRepository.getEventsByInitiatorId(userId).stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        if (eventIds.isEmpty()) {
            return participationRepository.getParticipantRequestsByRequester(userId).stream()
                    .map(MapperOfParticipant::toParticipationRequestDto)
                    .collect(Collectors.toList());
        }
        return participationRepository.getParticipantRequestsByRequesterAndEventNotIn(userId, eventIds).stream()
                .map(MapperOfParticipant::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParticipantRequestDto addPrivateParticipationRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        List<Participant> participationRequestList = participationRepository.getParticipantRequestsByRequesterAndEvent(userId, eventId);
        validatePrivateAddParticipationRequest(event, participationRequestList, userId);
        Participant newParticipationRequest =
                participationRepository.save(new Participant()
                        .setRequester(userId)
                        .setCreated(LocalDateTime.now())
                        .setEvent(eventId)
                        .setStatus(checkStatus(event) ? Status.CONFIRMED : Status.PENDING));
        eventRepository.save(event);
        ParticipantRequestDto participationRequestDto = MapperOfParticipant
                .toParticipationRequestDto(newParticipationRequest);
        participationRequestDto.setId(newParticipationRequest.getId());
        return participationRequestDto;
    }

    private boolean checkStatus(Event event) {
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            return true;
        }
        return false;
    }

    private void validatePrivateAddParticipationRequest(Event event, List<Participant> participationRequestList, Long userId) {
        if (participationRequestList.size() != 0) {
            throw new DoubleParticipationException("Ошибка. Повторная заявка.");
        }

        if (event == null) {
            throw new IllegalArgumentException("Событие не найдено.");
        } else if (event.getInitiator().getId().equals(userId)) {
            throw new DoubleParticipationException("Заявка на собственное мероприятие");
        } else if (event.getState() == null || !event.getState().equals(State.PUBLISHED)) {
            throw new DoubleParticipationException("Неверный статус.");
        } else if (event.getConfirmedRequests() != null
                && event.getParticipantLimit() > 0
                && event.getConfirmedRequests().equals(Long.valueOf(event.getParticipantLimit()))) {
            throw new OverflowLimitException("Превышен лимит участников.");
        }
    }

    public ParticipantRequestDto updatePrivateRejectedParticipationRequest(Long userId, Long requestId) {
        Participant participationRequest = participationRepository.getParticipantRequestByIdAndRequester(requestId, userId);
        if (participationRequest == null) {
            throw new NotFoundException("Заявка не найдена.");
        }
        if (participationRequest.getStatus().equals(Status.PENDING)) {
            participationRequest.setStatus(Status.CANCELED);
        } else if (participationRequest.getStatus().equals(Status.CONFIRMED)) {
            Optional<Event> event = eventRepository.findById(participationRequest.getEvent());
            event.ifPresent(e -> {
                e.setConfirmedRequests(e.getConfirmedRequests() - 1);
                eventRepository.save(e);
                participationRequest.setStatus(Status.CANCELED);
            });
        }
        return MapperOfParticipant.toParticipationRequestDto(participationRepository.save(participationRequest));
    }

}