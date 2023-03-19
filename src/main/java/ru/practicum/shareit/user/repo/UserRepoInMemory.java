package ru.practicum.shareit.user.repo;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRepoInMemory implements UserRepo {

    final Map<Long, User> repo = new HashMap<>();
    Long idCounter = 0L;

    public List<User> findAll() {
        return new ArrayList<User>(repo.values());
    }

    public User create(User user) {
        user.setId(++idCounter);
        repo.put(idCounter, user);
        return user;
    }

    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(repo.get(userId));
    }

    public void deleteById(Long userId) {
        repo.remove(userId);
    }

    public void deleteAll() {
        repo.clear();
    }

}
