package ru.practicum.shareit.item.comment.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.comment.repo.CommentRepo;

import java.util.Collections;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @InjectMocks
    CommentServiceImpl service;

    @Mock
    CommentRepo commentRepo;

    @Test
    public void givenItemId_whenFindingCommentsByItem_thenInvokeCommentRepo() {
        service.findByItemId(1L);
        Sort sort = Sort.by(Sort.Direction.ASC, "created");

        verify(commentRepo, times(1)).findByItemId(1L, sort);
    }

    @Test
    public void givenItemIds_whenFindingCommentsByItem_thenInvokeCommentRepo() {
        service.findByItemIn(Collections.singletonList(1L));
        Sort sort = Sort.by(Sort.Direction.DESC, "created");

        verify(commentRepo, times(1)).findByItemIdIn(Collections.singletonList(1L), sort);
    }

}