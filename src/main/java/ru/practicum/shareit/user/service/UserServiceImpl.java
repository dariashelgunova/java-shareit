package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundObjectException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepo userRepo;

    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Transactional
    public User create(User user) {
        return userRepo.save(user);
    }

    @Transactional
    public User update(User newUser, Long userId) {
        User oldUser = getUserByIdOrThrowException(userId);
        return changeUserFields(oldUser, newUser);
    }

    public User findById(Long userId) {
        return getUserByIdOrThrowException(userId);
    }

    @Transactional
    public void deleteById(Long userId) {
        userRepo.deleteById(userId);
    }

    @Transactional
    public void deleteAll() {
        userRepo.deleteAll();
    }

    private User getUserByIdOrThrowException(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundObjectException("Объект не был найден"));
    }

    private User changeUserFields(User oldUser, User newUser) {
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {

            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getName() != null && !newUser.getName().isBlank()) {
            oldUser.setName(newUser.getName());
        }
        return oldUser;
    }
}
