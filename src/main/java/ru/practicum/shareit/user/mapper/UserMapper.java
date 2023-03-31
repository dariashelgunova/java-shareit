package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public User fromDtoToUser(UserDto userDto) {
        if (userDto == null) return null;

        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        return user;
    }

    public UserDto fromUserToDto(User user) {
        if (user == null) return null;

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public List<UserDto> changeListFromUserToDto(List<User> users) {
        if (users == null) return null;

        return users
                .stream()
                .map(this::fromUserToDto)
                .collect(Collectors.toList());
    }
}
