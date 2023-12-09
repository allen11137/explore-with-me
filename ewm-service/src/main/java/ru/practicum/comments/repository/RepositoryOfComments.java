package ru.practicum.comments.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comments.model.Comments;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryOfComments extends JpaRepository<Comments, Long> {
    List<Comments> findCommentsByEventId(Long eventId, Pageable pageable);

    Optional<Comments> findById(Long id);
}