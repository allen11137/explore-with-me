package ru.practicum.comments.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentsRequestDto;
import ru.practicum.comments.dto.CommentsResponseDto;
import ru.practicum.comments.service.CommentsService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@Slf4j
@Validated
@RestController
@AllArgsConstructor
public class PrivateCommentsController {

    private final CommentsService commentService;

    @PostMapping("/comments")
    public ResponseEntity<CommentsResponseDto> addCommentForUser(@RequestBody CommentsRequestDto commentDto) {
        log.info("Post request add comment.");
        return new ResponseEntity<>(commentService.addCommentForUser(commentDto), HttpStatus.CREATED);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentsResponseDto> updateCommentForUser(@Positive @PathVariable Long commentId,
                                                                   @Valid @RequestBody CommentsRequestDto commentDto) {
        log.info("Updating request comment by user.");
        return ResponseEntity.ok(commentService.updateCommentForUser(commentId, commentDto));
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentsResponseDto> getCommentByIdForUser(@Positive @PathVariable Long commentId) {
        log.info("Get request comment by id.");
        return new ResponseEntity<>(commentService.getCommentByIdForUser(commentId), HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}/comments")
    public ResponseEntity<List<CommentsResponseDto>> getCommentsForUser(@PathVariable Long eventId,
                                                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                       @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get request all comment by user.");
        return new ResponseEntity<>(commentService.getCommentsForUser(eventId, from, size), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    public ResponseEntity<Void> deleteCommentByIdForUser(@Positive @PathVariable Long userId, @Positive @PathVariable Long commentId) {
        log.info("Delete request comment by user.");
        commentService.deleteCommentByIdForUser(userId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}