package ru.practicum.user.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.AddUserRequest;
import ru.practicum.user.dto.CompleteUserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/admin/users")
public class AdminUserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<CompleteUserDto> addUser(@Valid @NonNull @RequestBody AddUserRequest userRequest) {
        return new ResponseEntity<>(userService.addAdminUser(userRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Collection<CompleteUserDto>> getUsers(@RequestParam(required = false) Collection<Long> ids,
                                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return new ResponseEntity<>(userService.getAdminUsers(ids, from, size), HttpStatus.OK);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@NonNull @Positive @PathVariable("userId") Long userId) {
        userService.deleteAdminUser(userId);
    }
}