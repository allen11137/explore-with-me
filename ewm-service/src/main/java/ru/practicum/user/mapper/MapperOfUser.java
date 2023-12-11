package ru.practicum.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.user.dto.AddUserRequest;
import ru.practicum.user.dto.CompleteUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

@UtilityClass
public class MapperOfUser {

    public CompleteUserDto toUserDto(User user) {
        return new CompleteUserDto()
                .setId(user.getId())
                .setName(user.getName())
                .setEmail(user.getEmail());
    }

    public UserDto toUserBriefDto(User user) {
        return new UserDto()
                .setId(user.getId())
                .setName(user.getName());
    }

    public User toUser(AddUserRequest userRequest) {
        return new User()
                .setName(userRequest.getName())
                .setEmail(userRequest.getEmail());
    }
}