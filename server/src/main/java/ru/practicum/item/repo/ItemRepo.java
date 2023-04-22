package ru.practicum.item.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.item.model.Item;

import java.util.List;

public interface ItemRepo extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId, Pageable pageRequest);

    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(String name, String description, boolean available, Pageable pageRequest);

    List<Item> findByRequestId(Long requestId);

    List<Item> findByRequestIdIn(List<Long> requestId);
}

