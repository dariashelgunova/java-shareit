package ru.practicum.shareit.item.repo.db;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepo extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId);

}

