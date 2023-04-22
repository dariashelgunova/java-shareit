package ru.practicum.item.comment.service;

import ru.practicum.item.comment.model.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findByItemId(Long itemId);

    List<Comment> findByItemIn(List<Long> items);
}
