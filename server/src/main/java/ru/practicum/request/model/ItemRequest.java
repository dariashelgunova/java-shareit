package ru.practicum.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = {"requestor", "items"})
@EqualsAndHashCode(exclude = {"requestor", "items"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String description;
    LocalDateTime created;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "requestor_id")
    User requestor;
    @Transient
    List<Item> items;
}
