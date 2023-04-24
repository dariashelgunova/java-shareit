package ru.practicum.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.booking.model.Booking;
import ru.practicum.item.comment.model.Comment;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = {"owner", "comments", "lastBooking", "nextBooking"})
@EqualsAndHashCode(exclude = {"owner", "comments", "lastBooking", "nextBooking"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    @Column(name = "is_available")
    Boolean available;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "owner_id")
    User owner;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "request_id")
    ItemRequest request;
    @Transient
    List<Comment> comments;
    @Transient
    Booking lastBooking;
    @Transient
    Booking nextBooking;
}

