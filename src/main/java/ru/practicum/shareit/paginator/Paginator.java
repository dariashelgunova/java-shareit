package ru.practicum.shareit.paginator;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InvalidPageParametersException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Paginator<T> {
    public List<T> paginate(Page page, List<T> objects) {
        if (page.getFrom() == -1 && page.getSize() == -1) {
            return objects;
        }
        if (page.getFrom() < 0 || page.getSize() <= 0) {
            throw new InvalidPageParametersException("Неверно введены данные для вывода страницы!");
        }
        if (objects.isEmpty()) {
            return Collections.emptyList();
        }
        int firstObjectIndex = page.getFrom();

        if (firstObjectIndex < objects.size()) {
            return objects.stream()
                    .skip(firstObjectIndex)
                    .limit(page.getSize())
                    .collect(Collectors.toList());
        } else {
            throw new InvalidPageParametersException("Неверно введены данные для вывода страницы!");
        }
    }


}
