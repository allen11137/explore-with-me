package ru.practicum.comments.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.AuthorWithCommentsDto;
import ru.practicum.comments.dto.CommentsDto;
import ru.practicum.comments.mapper.MapperOfComment;
import ru.practicum.comments.model.Comments;
import ru.practicum.comments.repository.RepositoryOfComments;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class CommentsService {
    private final RepositoryOfComments commentRepository;
    private final UserService userService;
    private final EventService eventService;

    public CommentsDto addCommentForUser(CommentsDto commentDto) {
        User user = userService.getUserById(commentDto.getAuthor());
        Event event = eventService.findEventById(commentDto.getEvent());
        return MapperOfComment.toCommentDto(commentRepository.save(MapperOfComment.toComment(commentDto, user, event)));
    }

    public CommentsDto updateCommentForUser(Long commentId, CommentsDto commentDto) {
        User user = userService.getUserById(commentDto.getAuthor());
        Event event = eventService.findEventById(commentDto.getEvent());

        if (!commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment by id [" + commentId + "] not found."))
                .getAuthor()
                .getId().equals(commentDto.getAuthor())) {
            throw new NotFoundException("У пользователя нет комментариев.");
        }

        Comments actualComment = MapperOfComment.toComment(commentDto, user, event);
        Comments comment = getComment(commentId);
        actualComment.setId(comment.getId());
        if (actualComment.getText() == null || actualComment.getText().isBlank()) {
            actualComment.setText(comment.getText());
        } else {
            actualComment.setText(actualComment.getText());
        }
        if (actualComment.getCreatedOn() == null) {
            actualComment.setCreatedOn(comment.getCreatedOn());
        } else {
            actualComment.setCreatedOn(actualComment.getCreatedOn());
        }
        actualComment.setUpdatedOn(LocalDateTime.now());
        return MapperOfComment.toCommentDto(commentRepository.save(actualComment));
    }

    public List<CommentsDto> getCommentsForUser(Long eventId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<CommentsDto> commentsDto = commentRepository.findCommentsByEventId(eventId, pageable).stream()
                .map(MapperOfComment::toCommentDto)
                .collect(Collectors.toList());
        if (commentsDto.isEmpty()) {
            throw new NotFoundException("У события нет комментариев.");
        } else {
            return commentsDto;
        }
    }

    public CommentsDto getCommentByIdForUser(Long commentId) {
        return MapperOfComment.toCommentDto(getComment(commentId));
    }

    public void deleteCommentByIdForUser(Long userId, Long commentId) {
        Comments comment = getComment(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("У пользователя нет комментариев.");
        } else {
            commentRepository.deleteById(commentId);
        }
    }

    public List<AuthorWithCommentsDto> getCommentsForAdmin(Long eventId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<CommentsDto> commentsDto = commentRepository.findCommentsByEventId(eventId, pageable).stream()
                .map(MapperOfComment::toCommentDto)
                .collect(Collectors.toList());
        if (commentsDto.isEmpty()) {
            throw new NotFoundException("У события нет комментариев.");
        } else {
            return commentsDto.stream()
                    .map(commentDto -> MapperOfComment.toCommentWithFullAuthorDto(commentDto, userService.getUserById(commentDto.getAuthor())))
                    .collect(Collectors.toList());
        }
    }

    public AuthorWithCommentsDto getCommentByIdForAdmin(Long commentId) {
        CommentsDto commentDto = MapperOfComment.toCommentDto(getComment(commentId));
        return MapperOfComment.toCommentWithFullAuthorDto(commentDto, userService.getUserById(commentDto.getAuthor()));
    }

    public void deleteCommentByIdForAdmin(Long userId, Long commentId) {
        getComment(commentId);
        commentRepository.deleteById(commentId);
    }

    public Comments getComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> {
            throw new NotFoundException("Комментарий не существует.");
        });
    }
}