package ru.practicum.shareit.booking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
@ToString(exclude = {"booker", "item"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "start_date")
    LocalDateTime start;
    @Column(name = "end_date")
    LocalDateTime end;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "item_id")
    Item item;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "booker_id")
    User booker;
    @Enumerated(EnumType.STRING)
    Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id.equals(booking.id) && Objects.equals(start, booking.start) && Objects.equals(end, booking.end) && Objects.equals(item, booking.item) && Objects.equals(booker, booking.booker) && status == booking.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end, item, booker, status);
    }
}


