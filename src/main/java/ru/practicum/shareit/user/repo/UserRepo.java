package ru.practicum.shareit.user.repo;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo {
    List<User> findAll();

    User create(User user);

    User update(User user);

    Optional<User> findById(Long userId);

    void deleteById(Long userId);

    void deleteAll();
}
