package ru.practicum.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping
    public List<UserRequestDto> findAll() {
        return UserMapper.toUserRequestDtoList(userService.findAll());
    }

    @PostMapping
    public UserRequestDto create(@RequestBody UserRequestDto user) {
        User newUser = UserMapper.fromUserRequestDto(user);
        User createdUser = userService.create(newUser);
        return UserMapper.toUserRequestDto(createdUser);
    }

    @PatchMapping("/{userId}")
    public UserRequestDto update(@PathVariable("userId") Long userId,
                                 @RequestBody UserRequestDto user) {
        User newUser = UserMapper.fromUserRequestDto(user);
        User updatedUser = userService.update(newUser, userId);
        return UserMapper.toUserRequestDto(updatedUser);
    }

    @GetMapping("/{userId}")
    public UserRequestDto findById(@PathVariable("userId") Long userId) {
        User user = userService.findById(userId);
        return UserMapper.toUserRequestDto(user);
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
