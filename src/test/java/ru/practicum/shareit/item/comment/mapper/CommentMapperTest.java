package ru.practicum.shareit.item.comment.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.dto.CommentDtoToReturn;
import ru.practicum.shareit.item.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.comment.dto.CommentSimpleDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.item.comment.mapper.CommentMapper.*;

class CommentMapperTest {

    @Test
    void fromCommentRequestDtoTest() {
        CommentRequestDto dto = new CommentRequestDto(1L, "text");
        Comment comment = fromCommentRequestDto(dto, null, null);

        assertEquals(dto.getId(), comment.getId());
        assertEquals(dto.getText(), comment.getText());
        assertNotNull(comment.getCreated());

        dto = null;
        assertNull(fromCommentRequestDto(dto, null, null));
    }

    @Test
    void toCommentRequestDtoTest() {
        User author = new User(1L, "yandex@yandex.ru", "name");
        Comment comment = new Comment(1L, "text", null, author, null);
        CommentDtoToReturn dto = toCommentRequestDto(comment);

        assertEquals(comment.getId(), dto.getId());
        assertEquals(comment.getText(), dto.getText());
        assertEquals(comment.getAuthor().getName(), dto.getAuthorName());

        comment = null;
        assertNull(toCommentRequestDto(comment));
    }

    @Test
    void toCommentSimpleDtoTest() {
        User author = new User(1L, "yandex@yandex.ru", "name");
        Comment comment = new Comment(1L, "text", null, author, null);
        CommentSimpleDto dto = toCommentSimpleDto(comment);

        assertEquals(comment.getId(), dto.getId());
        assertEquals(comment.getText(), dto.getText());
        assertEquals(comment.getAuthor().getName(), dto.getAuthorName());

        comment = null;
        assertNull(toCommentSimpleDto(comment));
    }

    @Test
    void toCommentSimpleDtoListTest() {
        User author = new User(1L, "yandex@yandex.ru", "name");
        Comment comment = new Comment(1L, "text", null, author, null);
        List<Comment> comments = List.of(comment);

        List<CommentSimpleDto> dtos = toCommentSimpleDtoList(comments);

        assertEquals(comments.size(), dtos.size());

        comments = null;
        assertNull(toCommentSimpleDtoList(comments));
    }
}