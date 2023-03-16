package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User create(User user);

    User update(User user, Long userId);

    User findById(Long userId);

    void deleteById(Long userId);

    void deleteAll();
}
