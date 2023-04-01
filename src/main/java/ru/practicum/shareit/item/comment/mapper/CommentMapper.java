package ru.practicum.shareit.item.comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {
    public Comment fromDtoToComment(CommentDto commentDto, Item item, User author) {
        if (commentDto == null) return null;
        LocalDateTime currentTime = LocalDateTime.from(LocalDateTime.now().minus(1, ChronoUnit.SECONDS));

        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(currentTime);

        return comment;
    }

    public CommentDto fromCommentToDto(Comment comment) {
        if (comment == null) return null;

        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setItem(comment.getItem());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());

        return commentDto;
    }

    public List<CommentDto> mapToDtoList(List<Comment> comments) {
        if (comments == null) return null;

        return comments
                .stream()
                .map(this::fromCommentToDto)
                .collect(Collectors.toList());
    }

}
