package ru.practicum.participant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.participant.model.Participant;

import java.util.List;


public interface RepositoryOfParticipant extends JpaRepository<Participant, Long> {
    List<Participant> getParticipantRequestByIdIn(List<Long> requestId);

    List<Participant> getParticipantRequestsByRequesterAndEvent(Long userId, Long eventId);

    List<Participant> getParticipantRequestsByRequesterAndEventNotIn(Long userId, List<Long> eventIdList);

    List<Participant> getParticipantRequestsByRequester(Long userId);

    List<Participant> getParticipantRequestsByEvent(Long eventId);

    Participant getParticipantRequestByIdAndRequester(Long requestId, Long userId);
}
