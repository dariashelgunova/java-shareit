package ru.practicum.shareit.request.repo;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepo extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByRequestorId(Long requestorId, Sort sort);

    List<ItemRequest> findByRequestorIdNot(Long requestorId, Sort sort);

}
