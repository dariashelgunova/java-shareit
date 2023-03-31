package ru.practicum.shareit.item.comment.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.comment.model.Comment;

import java.util.List;

@Repository
public interface CommentRepoInDb extends JpaRepository<Comment, Long> {
    List<Comment> findByItemIdOrderByCreated(Long itemId);
}
