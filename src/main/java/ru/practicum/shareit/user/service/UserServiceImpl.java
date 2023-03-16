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
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepo userRepo;

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User create(User user) {
        if (isEmailAlreadyOccupied(user))
            throw new ValidationException("Данный адрес электронной почты уже присутствует в базе.");
        return userRepo.create(user);
    }

    public User update(User newUser, Long userId) {
        User oldUser = getUserByIdOrThrowException(userId);
        newUser.setId(userId);
        return userRepo.update(changeUserFields(oldUser, newUser));
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

    private boolean isEmailAlreadyOccupied(User userToCheck) {
        Map<Long, User> users = userRepo.findAll()
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        String userToCheckEmail = userToCheck.getEmail();
        boolean isNew = true;
        if (userToCheck.getId() == null || users.containsKey(userToCheck.getId())) {
            for (User currentUser : users.values()) {
                if (userToCheckEmail.equals(currentUser.getEmail()) &&
                        !Objects.equals(currentUser.getId(), userToCheck.getId())) {
                    isNew = false;
                    break;
                }
            }
        }
        return !isNew;
    }

    private User changeUserFields(User oldUser, User newUser) {
        if (newUser.getEmail() != null) {
            if (isEmailAlreadyOccupied(newUser))
                throw new ValidationException("Данный адрес электронной почты уже присутствует в базе.");
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getName() != null) {
            oldUser.setName(newUser.getName());
        }
        if (newUser.getLogin() != null) {
            oldUser.setLogin(newUser.getLogin());
        }
        if (newUser.getBirthday() != null) {
            oldUser.setBirthday(newUser.getBirthday());
        }
        return oldUser;
    }
}
