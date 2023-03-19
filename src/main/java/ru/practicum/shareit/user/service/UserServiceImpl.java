package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundObjectException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepo;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepo userRepo;

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User create(User user) {
        isEmailAlreadyOccupied(user);
        return userRepo.create(user);
    }

    public User update(User newUser, Long userId) {
        User oldUser = getUserByIdOrThrowException(userId);
        newUser.setId(userId);
        return changeUserFields(oldUser, newUser);
    }

    public User findById(Long userId) {
        return getUserByIdOrThrowException(userId);
    }

    public void deleteById(Long userId) {
        userRepo.deleteById(userId);
    }

    public void deleteAll() {
        userRepo.deleteAll();
    }

    private User getUserByIdOrThrowException(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundObjectException("Объект не был найден"));
    }

    private void isEmailAlreadyOccupied(User userToCheck) {
        userRepo.findAll()
                .stream()
                .filter(u -> !Objects.equals(u.getId(), userToCheck.getId()) && userToCheck.getEmail().equals(u.getEmail()))
                .findFirst()
                .ifPresent((u) -> {throw new ValidationException("Данный адрес электронной почты уже присутствует в базе.");});
    }

    private User changeUserFields(User oldUser, User newUser) {
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
            isEmailAlreadyOccupied(newUser);

            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getName() != null && !newUser.getName().isBlank()) {
            oldUser.setName(newUser.getName());
        }
        return oldUser;
    }
}
