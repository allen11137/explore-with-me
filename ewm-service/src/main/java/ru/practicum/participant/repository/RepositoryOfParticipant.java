package ru.practicum.participant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.participant.model.Participant;

import java.util.List;


public interface RepositoryOfParticipant extends JpaRepository<Participant, Long> {
    List<Participant> getParticipationRequestByIdIn(List<Long> requestId);

    List<Participant> getParticipationRequestsByRequesterAndEvent(Long userId, Long eventId);

    List<Participant> getParticipationRequestsByRequesterAndEventNotIn(Long userId, List<Long> eventIdList);

    List<Participant> getParticipationRequestsByRequester(Long userId);

    List<Participant> getParticipationRequestsByEvent(Long eventId);

    Participant getParticipationRequestByIdAndRequester(Long requestId, Long userId);
}