package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank(message = "Имя не может быть пустым. Попробуйте еще раз.")
    String name;
    @NotBlank(message = "Описание не может быть пустым. Попробуйте еще раз.")
    @Size(max = 200, message = "Максимальная длина описания составляет 200 символов")
    String description;
    @NotNull
    @Column(name = "is_available")
    Boolean available;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    User owner;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id")
    ItemRequest request;
}

