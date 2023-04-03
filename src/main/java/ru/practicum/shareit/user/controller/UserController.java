package ru.practicum.shareit.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.mapper.Mapper;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    Mapper mapper;

    @GetMapping
    public List<UserRequestDto> findAll() {
        return mapper.toUserRequestDtoList(userService.findAll());
    }

    @PostMapping
    public UserRequestDto create(@Validated(Create.class) @RequestBody UserRequestDto user) {
        User newUser = mapper.fromUserRequestDto(user);
        User createdUser = userService.create(newUser);
        return mapper.toUserRequestDto(createdUser);
    }

    @PatchMapping("/{userId}")
    public UserRequestDto update(@PathVariable("userId") Long userId, @Validated(Update.class) @RequestBody UserRequestDto user) {
        User newUser = mapper.fromUserRequestDto(user);
        User updatedUser = userService.update(newUser, userId);
        return mapper.toUserRequestDto(updatedUser);
    }

    @GetMapping("/{userId}")
    public UserRequestDto findById(@PathVariable("userId") Long userId) {
        User user = userService.findById(userId);
        return mapper.toUserRequestDto(user);
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
