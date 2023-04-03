package ru.practicum.shareit.item.comment.repo;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.comment.model.Comment;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByItemId(Long itemId, Sort sort);

    List<Comment> findByItemIdIn(List<Long> itemId, Sort sort);
}
