package ru.practicum.shareit.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import java.util.List;

import static ru.practicum.shareit.user.mapper.UserMapper.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping
    public List<UserRequestDto> findAll() {
        return toUserRequestDtoList(userService.findAll());
    }

    @PostMapping
    public UserRequestDto create(@Validated(Create.class) @RequestBody UserRequestDto user) {
        User newUser = fromUserRequestDto(user);
        User createdUser = userService.create(newUser);
        return toUserRequestDto(createdUser);
    }

    @PatchMapping("/{userId}")
    public UserRequestDto update(@PathVariable("userId") Long userId,
                                 @Validated(Update.class) @RequestBody UserRequestDto user) {
        User newUser = fromUserRequestDto(user);
        User updatedUser = userService.update(newUser, userId);
        return toUserRequestDto(updatedUser);
    }

    @GetMapping("/{userId}")
    public UserRequestDto findById(@PathVariable("userId") Long userId) {
        User user = userService.findById(userId);
        return toUserRequestDto(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable("userId") Long userId) {
        userService.deleteById(userId);
    }

    @DeleteMapping
    public void deleteAll() {
        userService.deleteAll();
    }

}
