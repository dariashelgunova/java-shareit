package ru.practicum.shareit.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * TODO Sprint add-item-requests.
 */
@Entity
@Getter
@Setter
@ToString(exclude = {"requestor"})
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
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "requestor_id")
    User requestor;
    @Transient
    List<Item> items;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequest that = (ItemRequest) o;
        return id.equals(that.id) && Objects.equals(requestor, that.requestor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestor);
    }
}
