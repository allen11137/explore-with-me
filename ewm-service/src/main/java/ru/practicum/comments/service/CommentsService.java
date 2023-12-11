package ru.practicum.comments.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.AuthorWithCommentsDto;
import ru.practicum.comments.dto.CommentsRequestDto;
import ru.practicum.comments.dto.CommentsResponseDto;
import ru.practicum.comments.mapper.MapperOfComment;
import ru.practicum.comments.model.Comments;
import ru.practicum.comments.repository.RepositoryOfComments;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.AuthorValidationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class CommentsService {
    private final RepositoryOfComments commentRepository;
    private final UserService userService;
    private final EventService eventService;

    public CommentsResponseDto addCommentForUser(CommentsRequestDto commentDto) {
        User user = userService.getUserById(commentDto.getAuthor());
        Event event = eventService.findEventById(commentDto.getEvent());
        return MapperOfComment.toCommentDto(commentRepository.save(MapperOfComment.toComment(commentDto, user, event)));
    }

    public CommentsResponseDto updateCommentForUser(Long commentId, CommentsRequestDto commentDto) {
        User user = userService.getUserById(commentDto.getAuthor());
        Comments comment = getComment(commentId);

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new AuthorValidationException("Пользователь с id " + user.getId() + " не может обновить комментарий.");
        }

        String text = commentDto.getText();
        if (text != null && !text.isBlank()) {
            comment.setText(text);
        }
        comment.setUpdatedOn(LocalDateTime.now());
        return MapperOfComment.toCommentDto(commentRepository.save(comment));
    }

    public List<CommentsResponseDto> getCommentsForUser(Long eventId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<CommentsResponseDto> commentsDto = commentRepository.findCommentsByEventId(eventId, pageable).stream()
                .map(MapperOfComment::toCommentDto)
                .collect(Collectors.toList());
        if (commentsDto.isEmpty()) {
            return Collections.emptyList();
        } else {
            return commentsDto;
        }
    }

    public CommentsResponseDto getCommentByIdForUser(Long commentId) {
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
        List<CommentsResponseDto> commentsDto = commentRepository.findCommentsByEventId(eventId, pageable).stream()
                .map(MapperOfComment::toCommentDto)
                .collect(Collectors.toList());
        if (commentsDto.isEmpty()) {
            return Collections.emptyList();
        } else {
            return commentsDto.stream()
                    .map(commentDto -> MapperOfComment.toCommentWithFullAuthorDto(commentDto, userService.getUserById(commentDto.getAuthor())))
                    .collect(Collectors.toList());
        }
    }

    public AuthorWithCommentsDto getCommentByIdForAdmin(Long commentId) {
        CommentsResponseDto commentDto = MapperOfComment.toCommentDto(getComment(commentId));
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