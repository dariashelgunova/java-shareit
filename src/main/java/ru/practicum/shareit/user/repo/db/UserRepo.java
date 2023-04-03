package ru.practicum.shareit.user.repo.db;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

public interface UserRepo extends JpaRepository<User, Long> {

}
