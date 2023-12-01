package ru.practicum.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.DoubleEmailException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.AddUserRequest;
import ru.practicum.user.dto.CompleteUserDto;
import ru.practicum.user.mapper.MapperOfUser;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.RepositoryOfUser;

import java.util.Collection;
import java.util.stream.Collectors;


@Slf4j
@Service
public class UserService {
    private final RepositoryOfUser userRepository;

    @Autowired
    public UserService(RepositoryOfUser userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Collection<CompleteUserDto> getUsersAdmin(Collection<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (ids == null || ids.size() == 0) {
            return userRepository.findAll(pageable).stream()
                    .map(MapperOfUser::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.getUsersByIdIn(ids, pageable).stream()
                    .map(MapperOfUser::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public CompleteUserDto addUserAdmin(AddUserRequest userRequest) {
        User user = MapperOfUser.toUser(userRequest);
        try {
            return MapperOfUser.toUserDto(userRepository.saveAndFlush(user));
        } catch (DataIntegrityViolationException e) {
            log.info("Duplicate email address");
            throw new DoubleEmailException(userRequest.getEmail());
        }
    }

    @Transactional
    public void deleteUserAdmin(Long userId) {
        getUserById(userId);
        userRepository.removeUserById(userId);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User not found by id.");
        });
    }
}