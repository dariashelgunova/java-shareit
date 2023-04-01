package ru.practicum.shareit.item.comment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String text;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    Item item;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    User author;
    @JsonFormat(pattern = "YYYY-mm-dd'T'hh:MM:ss")
    @DateTimeFormat(pattern = "YYYY-mm-dd'T'hh:MM:ss")
    LocalDateTime created;
}
