package ru.practicum.shareit.item.comment.service;

import ru.practicum.shareit.item.comment.model.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findByItemId(Long itemId);

    List<Comment> findByItemIn(List<Long> items);
}
