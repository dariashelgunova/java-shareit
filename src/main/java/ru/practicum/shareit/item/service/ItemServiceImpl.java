package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.AvailabilityException;
import ru.practicum.shareit.exception.NotFoundObjectException;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repo.CommentRepo;
import ru.practicum.shareit.item.comment.service.CommentService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.db.ItemRepo;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ItemServiceImpl implements ItemService {
    ItemRepo itemRepo;
    UserService userService;
    CommentRepo commentRepo;
    CommentService commentService;
    BookingService bookingService;

    public List<Item> findAll() {
        return itemRepo.findAll();
    }

    @Transactional
    public Item create(Item newItem, Long ownerId) {
        User owner = userService.findById(ownerId);
        newItem.setOwner(owner);
        return itemRepo.save(newItem);
    }

    @Transactional
    public Item update(Item newItem, Long ownerId, Long itemId) {
        Item oldItem = getItemByIdOrThrowException(itemId);
        if (Objects.equals(oldItem.getOwner().getId(), ownerId)) {
            //return itemRepo.save(changeItemFields(oldItem, newItem));
            return changeItemFields(oldItem, newItem);
        } else {
            throw new AccessDeniedException("Только собственник может редактировать вещь!");
        }
    }


    public Item findById(Long itemId) {
        return getItemByIdOrThrowException(itemId);
    }

    @Transactional
    public void deleteById(Long itemId) {
        itemRepo.deleteById(itemId);
    }

    @Transactional
    public void deleteAll() {
        itemRepo.deleteAll();
    }

    public List<Item> findItemsByOwner(Long ownerId) {
        List<Item> result = itemRepo.findByOwnerId(ownerId);
        if (result.isEmpty()) {
            throw new NotFoundObjectException("По вашему запросу ничего найдено.");
        } else {
            return result;
        }
    }

    public List<Item> findItemsBySearch(String requestText) {
        if (StringUtils.isBlank(requestText)) return Collections.emptyList();
        List<Item> result = itemRepo.findAll()
                .stream()
                .filter(o -> (o.getName().toLowerCase().contains(requestText.toLowerCase())
                        || o.getDescription().toLowerCase().contains(requestText.toLowerCase()))
                        && o.getAvailable())
                .collect(toList());
        if (result.isEmpty()) {
            throw new NotFoundObjectException("По вашему запросу ничего найдено.");
        } else {
            return result;
        }
    }

    @Transactional
    public Comment addComment(Long itemId, Long userId, Comment newComment) {
        List<Booking> result = bookingService.findBookingsByUserForComment(userId, State.PAST)
                .stream()
                .filter(b -> b.getItem().getId().equals(itemId))
                .collect(toList());

        if (!result.isEmpty()) {
            return commentRepo.save(newComment);
        } else {
            throw new AvailabilityException("Пользователь еще не брал в аренду данную вещь и не может оставить комментарий!");
        }
    }

    private Item getItemByIdOrThrowException(Long itemId) {
        return itemRepo.findById(itemId)
                .orElseThrow(() -> new NotFoundObjectException("Объект не был найден"));
    }

    private Item changeItemFields(Item oldItem, Item newItem) {
        if (newItem.getName() != null && !newItem.getName().isBlank()) {
            oldItem.setName(newItem.getName());
        }
        if (newItem.getDescription() != null && !newItem.getDescription().isBlank()) {
            oldItem.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            oldItem.setAvailable(newItem.getAvailable());
        }
        return oldItem;
    }

    public Item addCommentsAndBookingsToItem(Item item, Long userId) {
        List<Comment> comments = commentService.findByItemId(item.getId());
        if (comments != null) {
            item.setComments(comments);
        }

        Booking lastBooking = bookingService.findLastBookingByItemId(item.getId());
        if (bookingService.checkAccessForItemLastNextBookingByUserId(lastBooking, userId)) {
            item.setLastBooking(lastBooking);
        }

        Booking nextBooking = bookingService.findNextBookingByItemId(item.getId());
        if (bookingService.checkAccessForItemLastNextBookingByUserId(nextBooking, userId)) {
            item.setNextBooking(nextBooking);
        }

        return item;
    }

    public List<Item> addCommentsAndBookingsToItems(List<Item> itemsByOwner, Long userId) {
        List<Long> items = itemsByOwner
                .stream()
                .map(Item::getId)
                .collect(toList());

        Map<Item, List<Comment>> comments = commentService.findByItemIn(items)
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));

        Map<Item, List<Booking>> lastBookings = bookingService.findLastBookingByItemIds(items)
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));

        Map<Item, List<Booking>> nextBookings = bookingService.findNextBookingByItemIds(items)
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));

        for (Item item : itemsByOwner) {
            item.setComments(comments.get(item));

            if (lastBookings.containsKey(item) && !lastBookings.get(item).isEmpty()) {
                item.setLastBooking(lastBookings.get(item).get(0));
            }
            if (nextBookings.containsKey(item) && !nextBookings.get(item).isEmpty()) {
                item.setNextBooking(nextBookings.get(item).get(0));
            }
        }
        return itemsByOwner;
    }

}
