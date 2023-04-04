package ru.practicum.shareit.item.comment.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repo.CommentRepo;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {
    CommentRepo commentRepo;

    public List<Comment> findByItemId(Long itemId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "created");
        return commentRepo.findByItemId(itemId, sort);
    }

    public List<Comment> findByItemIn(List<Long> items) {

        return commentRepo.findByItemIdIn(items, Sort.by(DESC, "created"));
    }

}
