package ru.practicum.comments.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.AuthorWithCommentsDto;
import ru.practicum.comments.service.CommentsService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@Slf4j
@Validated
@Controller
@AllArgsConstructor
@RequestMapping(path = "/admin")
public class AdminCommentsController {

    private final CommentsService commentService;

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    public ResponseEntity<Void> deleteCommentByIdForAdmin(@Positive @PathVariable Long userId, @Positive @PathVariable Long commentId) {
        log.info("Delete request comment by admin.");
        commentService.deleteCommentByIdForAdmin(userId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/events/{eventId}/comments")
    public ResponseEntity<List<AuthorWithCommentsDto>> getCommentsForAdmin(@PathVariable Long eventId,
                                                                           @PositiveOrZero @RequestParam(name = "from",
                                                                                   defaultValue = "0") Integer from,
                                                                           @Positive @RequestParam(name = "size",
                                                                                   defaultValue = "10") Integer size) {
        log.info("Get request all comments for admin.");
        return new ResponseEntity<>(commentService.getCommentsForAdmin(eventId, from, size), HttpStatus.OK);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<AuthorWithCommentsDto> getCommentByIdForAdmin(@Positive @PathVariable Long commentId) {
        log.info("Get request comment by id for admin.");
        return new ResponseEntity<>(commentService.getCommentByIdForAdmin(commentId), HttpStatus.OK);
    }
}