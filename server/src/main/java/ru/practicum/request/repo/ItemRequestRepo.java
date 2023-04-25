package ru.practicum.request.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepo extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByRequestorId(Long requestorId, Sort sort);

    List<ItemRequest> findByRequestorIdNot(Long requestorId, Pageable pageRequest);

}
