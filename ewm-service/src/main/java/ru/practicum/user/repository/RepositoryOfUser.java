package ru.practicum.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.user.model.User;

import java.util.Collection;
import java.util.List;

@Repository
public interface RepositoryOfUser extends JpaRepository<User, Long> {

    User save(User user);

    List<User> getUsersByIdIn(Collection<Long> ids, Pageable pageable);

    void removeUserById(Long userId);

    User getUserById(Long userId);
}