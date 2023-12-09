package ru.practicum.comments.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.comments.dto.AuthorWithCommentsDto;
import ru.practicum.comments.dto.CommentsDto;
import ru.practicum.comments.model.Comments;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;


@UtilityClass
public class MapperOfComment {

    public static AuthorWithCommentsDto toCommentWithFullAuthorDto(CommentsDto commentDto, User user) {
        return new AuthorWithCommentsDto()
                .setId(commentDto.getId())
                .setText(commentDto.getText())
                .setEvent(commentDto.getEvent())
                .setCreatedOn(commentDto.getCreatedOn())
                .setUpdatedOn(commentDto.getUpdatedOn())
                .setAuthor(user);
    }

    public static CommentsDto toCommentDto(Comments comment) {
        return new CommentsDto()
                .setId(comment.getId())
                .setText(comment.getText())
                .setEvent(comment.getEvent().getId())
                .setCreatedOn(comment.getCreatedOn())
                .setUpdatedOn(comment.getUpdatedOn())
                .setAuthor(comment.getAuthor().getId());
    }

    public static Comments toComment(CommentsDto commentDto, User user, Event event) {
        return new Comments()
                .setText(commentDto.getText())
                .setEvent(event)
                .setAuthor(user)
                .setCreatedOn(LocalDateTime.now());
    }
}