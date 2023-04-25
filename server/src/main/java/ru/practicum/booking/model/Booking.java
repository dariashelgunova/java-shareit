package ru.practicum.booking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
@EqualsAndHashCode(exclude = {"booker", "item"})
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
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "item_id")
    Item item;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "booker_id")
    User booker;
    @Enumerated(EnumType.STRING)
    Status status;
}


