package ru.practicum.shareit.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    UserMapper userMapper;

    @GetMapping
    public List<UserDto> findAll() {
        return changeListFromUserToDto(userService.findAll());
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) {
        User newUser = userMapper.fromDtoToUser(user);
        User createdUser = userService.create(newUser);
        return userMapper.fromUserToDto(createdUser);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable("userId") Long userId, @RequestBody UserDto user) {
        User newUser = userMapper.fromDtoToUser(user);
        User updatedUser = userService.update(newUser, userId);
        return userMapper.fromUserToDto(updatedUser);
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable("userId") Long userId) {
        User user = userService.findById(userId);
        return userMapper.fromUserToDto(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable("userId") Long userId) {
        userService.deleteById(userId);
    }

    @DeleteMapping
    public void deleteAll() {
        userService.deleteAll();
    }

    private List<UserDto> changeListFromUserToDto(List<User> users) {
        List<UserDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(userMapper.fromUserToDto(user));
        }
        return dtos;
    }
}
