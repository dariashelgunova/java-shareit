package ru.practicum.shareit.item.comment.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repo.CommentRepoInDb;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {
    CommentRepoInDb commentRepo;

    public List<Comment> findAllByItemId(Long itemId) {
        return commentRepo.findByItemIdOrderByCreated(itemId);
    }

}
