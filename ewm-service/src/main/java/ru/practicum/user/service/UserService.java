package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class UserService {
    private final RepositoryOfUser userRepository;

    @Transactional(readOnly = true)
    public Collection<CompleteUserDto> getAdminUsers(Collection<Long> ids, Integer from, Integer size) {
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

    public CompleteUserDto addAdminUser(AddUserRequest userRequest) {
        User user = MapperOfUser.toUser(userRequest);
        try {
            return MapperOfUser.toUserDto(userRepository.saveAndFlush(user));
        } catch (DataIntegrityViolationException e) {
            log.info("Такой email уже существует");
            throw new DoubleEmailException(userRequest.getEmail());
        }
    }

    public void deleteAdminUser(Long userId) {
        getUserById(userId);
        userRepository.removeUserById(userId);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}