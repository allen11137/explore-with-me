package ru.practicum.comments.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.comments.dto.AuthorWithCommentsDto;
import ru.practicum.comments.dto.CommentsRequestDto;
import ru.practicum.comments.dto.CommentsResponseDto;
import ru.practicum.comments.model.Comments;
import ru.practicum.event.model.Event;
import ru.practicum.user.mapper.MapperOfUser;
import ru.practicum.user.model.User;


@UtilityClass
public class MapperOfComment {

    public AuthorWithCommentsDto toCommentWithFullAuthorDto(CommentsResponseDto commentDto, User user) {
        return new AuthorWithCommentsDto()
                .setId(commentDto.getId())
                .setText(commentDto.getText())
                .setEvent(commentDto.getEvent())
                .setCreatedOn(commentDto.getCreatedOn())
                .setUpdatedOn(commentDto.getUpdatedOn())
                .setAuthor(MapperOfUser.toUserDto(user));
    }

    public CommentsResponseDto toCommentDto(Comments comment) {
        return new CommentsResponseDto()
                .setId(comment.getId())
                .setText(comment.getText())
                .setEvent(comment.getEvent().getId())
                .setCreatedOn(comment.getCreatedOn())
                .setUpdatedOn(comment.getUpdatedOn())
                .setAuthor(comment.getAuthor().getId());
    }

    public Comments toComment(CommentsRequestDto commentDto, User user, Event event) {
        return new Comments()
                .setText(commentDto.getText())
                .setEvent(event)
                .setAuthor(user);
    }
}