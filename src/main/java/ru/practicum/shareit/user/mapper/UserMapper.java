package ru.practicum.shareit.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserSimpleDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public static User fromUserRequestDto(UserRequestDto userRequestDto) {
        if (userRequestDto == null) return null;

        User user = new User();
        user.setId(userRequestDto.getId());
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());

        return user;
    }

    public static UserRequestDto toUserRequestDto(User user) {
        if (user == null) return null;

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setId(user.getId());
        userRequestDto.setName(user.getName());
        userRequestDto.setEmail(user.getEmail());

        return userRequestDto;
    }

    public static List<UserRequestDto> toUserRequestDtoList(List<User> users) {
        if (users == null) return null;

        return users
                .stream()
                .map(UserMapper::toUserRequestDto)
                .collect(Collectors.toList());
    }

    public static UserSimpleDto toUserSimpleDto(User user) {
        if (user == null) return null;

        UserSimpleDto userRequestDto = new UserSimpleDto();
        userRequestDto.setId(user.getId());

        return userRequestDto;
    }

    public static List<UserSimpleDto> toUserSimpleDto(List<User> users) {
        if (users == null) return null;

        return users
                .stream()
                .map(UserMapper::toUserSimpleDto)
                .collect(Collectors.toList());
    }


}
