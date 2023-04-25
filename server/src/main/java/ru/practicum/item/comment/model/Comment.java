package ru.practicum.item.comment.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString(exclude = {"item", "author"})
@EqualsAndHashCode(exclude = {"item", "author"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String text;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "item_id")
    Item item;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "author_id")
    User author;
    LocalDateTime created;
}
