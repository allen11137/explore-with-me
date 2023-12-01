package ru.practicum.user.mapper;

import ru.practicum.user.dto.AddUserRequest;
import ru.practicum.user.dto.CompleteUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

public class MapperOfUser {

    public static CompleteUserDto toUserDto(User user) {
        return new CompleteUserDto()
                .setId(user.getId())
                .setName(user.getName())
                .setEmail(user.getEmail());
    }

    public static UserDto toUserShortDto(User user) {
        return new UserDto()
                .setId(user.getId())
                .setName(user.getName());
    }

    public static User toUser(AddUserRequest userRequest) {
        return new User()
                .setName(userRequest.getName())
                .setEmail(userRequest.getEmail());
    }
}