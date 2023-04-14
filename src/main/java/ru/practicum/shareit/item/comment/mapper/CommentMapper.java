package ru.practicum.shareit.item.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.comment.dto.CommentDtoToReturn;
import ru.practicum.shareit.item.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.comment.dto.CommentSimpleDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {

    public static Comment fromCommentRequestDto(CommentRequestDto commentDto, Item item, User author) {
        if (commentDto == null) return null;
        LocalDateTime currentTime = LocalDateTime.from(LocalDateTime.now());

        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(currentTime);

        return comment;
    }

    public static CommentDtoToReturn toCommentRequestDto(Comment comment) {
        if (comment == null) return null;

        CommentDtoToReturn commentDtoToReturn = new CommentDtoToReturn();
        commentDtoToReturn.setId(comment.getId());
        commentDtoToReturn.setText(comment.getText());
        commentDtoToReturn.setAuthorName(comment.getAuthor().getName());
        commentDtoToReturn.setCreated(comment.getCreated());

        return commentDtoToReturn;
    }

    public static CommentSimpleDto toCommentSimpleDto(Comment comment) {
        if (comment == null) return null;

        CommentSimpleDto commentDtoToReturn = new CommentSimpleDto();
        commentDtoToReturn.setId(comment.getId());
        commentDtoToReturn.setText(comment.getText());
        commentDtoToReturn.setAuthorName(comment.getAuthor().getName());
        commentDtoToReturn.setCreated(comment.getCreated());

        return commentDtoToReturn;
    }

    public static List<CommentSimpleDto> toCommentSimpleDtoList(List<Comment> comments) {
        if (comments == null) return null;

        return comments
                .stream()
                .map(CommentMapper::toCommentSimpleDto)
                .collect(Collectors.toList());
    }
}
