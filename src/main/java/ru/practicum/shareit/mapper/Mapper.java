package ru.practicum.shareit.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoToReturn;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.dto.CommentDtoToReturn;
import ru.practicum.shareit.item.comment.dto.CommentSimpleDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemSimpleDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserSimpleDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Mapper {

    //ITEM
    public Item fromItemRequestDto(ItemRequestDto itemRequestDto) {
        if (itemRequestDto == null) return null;

        Item item = new Item();
        item.setId(itemRequestDto.getId());
        item.setName(itemRequestDto.getName());
        item.setDescription(itemRequestDto.getDescription());
        item.setAvailable(itemRequestDto.getAvailable());

        return item;
    }

    public ItemRequestDto toItemRequestDto(Item item) {
        if (item == null) return null;

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(item.getId());
        itemRequestDto.setName(item.getName());
        itemRequestDto.setDescription(item.getDescription());
        itemRequestDto.setAvailable(item.getAvailable());

        return itemRequestDto;
    }

    public List<ItemRequestDto> toItemRequestDtoList(List<Item> items) {
        if (items == null) return null;

        return items
                .stream()
                .map(this::toItemRequestDto)
                .collect(Collectors.toList());
    }

    public ItemDtoWithComments toItemDtoWithComments(Item item, List<Comment> comments) {
        if (item == null) return null;

        ItemDtoWithComments itemDto = new ItemDtoWithComments();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setComments(toCommentSimpleDtoList(comments));

        return itemDto;
    }

    public ItemDtoForOwner toItemDtoForOwner(Item item) {
        if (item == null) return null;

        ItemDtoForOwner itemDto = new ItemDtoForOwner();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setComments(toCommentSimpleDtoList(item.getComments()));
        itemDto.setLastBooking(toBookingSimpleDto(item.getLastBooking()));
        itemDto.setNextBooking(toBookingSimpleDto(item.getNextBooking()));

        return itemDto;
    }

    public List<ItemDtoForOwner> toItemDtoForOwnerList(List<Item> items) {
        if (items == null) return null;

        return items
                .stream()
                .map(this::toItemDtoForOwner)
                .collect(Collectors.toList());
    }

    public ItemSimpleDto toItemSimpleDto(Item item) {
        if (item == null) return null;

        ItemSimpleDto itemDto = new ItemSimpleDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());

        return itemDto;
    }

    public List<ItemSimpleDto> toItemSimpleDtoList(List<Item> items) {
        if (items == null) return null;

        return items
                .stream()
                .map(this::toItemSimpleDto)
                .collect(Collectors.toList());
    }


    //BOOKING
    public Booking fromBookingRequestDto(BookingRequestDto bookingRequestDto, Item item, User user) {
        if (bookingRequestDto == null) return null;

        Booking booking = new Booking();
        booking.setId(bookingRequestDto.getId());
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);

        return booking;
    }

    public BookingRequestDto toBookingRequestDto(Booking booking) {
        if (booking == null) return null;

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setId(booking.getId());
        bookingRequestDto.setStart(booking.getStart());
        bookingRequestDto.setEnd(booking.getEnd());
        bookingRequestDto.setItemId(booking.getItem().getId());

        return bookingRequestDto;
    }

    public List<BookingRequestDto> toBookingRequestDtoList(List<Booking> bookings) {
        if (bookings == null) return null;

        return bookings
                .stream()
                .map(this::toBookingRequestDto)
                .collect(Collectors.toList());
    }

    public BookingDtoToReturn toBookingDtoToReturn(Booking booking) {
        if (booking == null) return null;

        BookingDtoToReturn bookingDto = new BookingDtoToReturn();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(toItemSimpleDto(booking.getItem()));
        bookingDto.setBooker(toUserSimpleDto(booking.getBooker()));
        bookingDto.setStatus(booking.getStatus());

        return bookingDto;
    }

    public List<BookingDtoToReturn> toBookingDtoToReturnList(List<Booking> bookings) {
        if (bookings == null) return null;

        return bookings
                .stream()
                .map(this::toBookingDtoToReturn)
                .collect(Collectors.toList());
    }

    public BookingSimpleDto toBookingSimpleDto(Booking booking) {
        if (booking == null) return null;

        BookingSimpleDto bookingDto = new BookingSimpleDto();
        bookingDto.setId(booking.getId());
        bookingDto.setBookerId(booking.getBooker().getId());

        return bookingDto;
    }

    public List<BookingSimpleDto> toBookingSimpleDtoList(List<Booking> bookings) {
        if (bookings == null) return null;

        return bookings
                .stream()
                .map(this::toBookingSimpleDto)
                .collect(Collectors.toList());
    }


    //COMMENT

    public Comment fromCommentRequestDto(CommentDtoToReturn commentDtoToReturn, Item item, User author) {
        if (commentDtoToReturn == null) return null;
        LocalDateTime currentTime = LocalDateTime.from(LocalDateTime.now().minus(1, ChronoUnit.SECONDS));

        Comment comment = new Comment();
        comment.setId(commentDtoToReturn.getId());
        comment.setText(commentDtoToReturn.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(currentTime);

        return comment;
    }

    public CommentDtoToReturn toCommentRequestDto(Comment comment) {
        if (comment == null) return null;

        CommentDtoToReturn commentDtoToReturn = new CommentDtoToReturn();
        commentDtoToReturn.setId(comment.getId());
        commentDtoToReturn.setText(comment.getText());
        commentDtoToReturn.setAuthorName(comment.getAuthor().getName());
        commentDtoToReturn.setCreated(comment.getCreated());

        return commentDtoToReturn;
    }

    public List<CommentDtoToReturn> toCommentRequestDtoList(List<Comment> comments) {
        if (comments == null) return null;

        return comments
                .stream()
                .map(this::toCommentRequestDto)
                .collect(Collectors.toList());
    }

    public CommentSimpleDto toCommentSimpleDto(Comment comment) {
        if (comment == null) return null;

        CommentSimpleDto commentDtoToReturn = new CommentSimpleDto();
        commentDtoToReturn.setId(comment.getId());
        commentDtoToReturn.setText(comment.getText());
        commentDtoToReturn.setAuthorName(comment.getAuthor().getName());
        commentDtoToReturn.setCreated(comment.getCreated());

        return commentDtoToReturn;
    }

    public List<CommentSimpleDto> toCommentSimpleDtoList(List<Comment> comments) {
        if (comments == null) return null;

        return comments
                .stream()
                .map(this::toCommentSimpleDto)
                .collect(Collectors.toList());
    }

    //USER

    public User fromUserRequestDto(UserRequestDto userRequestDto) {
        if (userRequestDto == null) return null;

        User user = new User();
        user.setId(userRequestDto.getId());
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());

        return user;
    }

    public UserRequestDto toUserRequestDto(User user) {
        if (user == null) return null;

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setId(user.getId());
        userRequestDto.setName(user.getName());
        userRequestDto.setEmail(user.getEmail());

        return userRequestDto;
    }

    public List<UserRequestDto> toUserRequestDtoList(List<User> users) {
        if (users == null) return null;

        return users
                .stream()
                .map(this::toUserRequestDto)
                .collect(Collectors.toList());
    }

    public UserSimpleDto toUserSimpleDto(User user) {
        if (user == null) return null;

        UserSimpleDto userRequestDto = new UserSimpleDto();
        userRequestDto.setId(user.getId());

        return userRequestDto;
    }

    public List<UserSimpleDto> toUserSimpleDto(List<User> users) {
        if (users == null) return null;

        return users
                .stream()
                .map(this::toUserSimpleDto)
                .collect(Collectors.toList());
    }

}
