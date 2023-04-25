package ru.practicum.item.comment.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.item.comment.model.Comment;
import ru.practicum.item.model.Item;
import ru.practicum.item.repo.ItemRepo;
import ru.practicum.user.model.User;
import ru.practicum.user.repo.UserRepo;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CommentRepoTest {

    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CommentRepo commentRepo;
    private User owner;
    private User author;
    private Item createdItem;
    private Comment comment;

    @BeforeEach
    public void init() {
        User itemOwner = createUser(2L);
        owner = userRepo.save(itemOwner);

        User commentAuthor = createUser(3L);
        author = userRepo.save(commentAuthor);

        Item item = createItem(1L, owner);
        createdItem = itemRepo.save(item);

        Comment newComment = createComment(1L, author, createdItem);
        comment = commentRepo.save(newComment);
    }

    @Test
    public void givenComment_whenFindingByItemId_thenReturnComment() {
        Sort sort = Sort.by(Sort.Direction.ASC, "created");
        assertEquals(1, commentRepo.findByItemId(createdItem.getId(), sort).size());
    }

    @Test
    public void givenComment_whenFindingByWrongItemId_thenReturnEmptyList() {
        Sort sort = Sort.by(Sort.Direction.ASC, "created");
        assertEquals(0, commentRepo.findByItemId(5L, sort).size());
    }

    @Test
    public void givenComment_whenFindingByItemIdIn_thenReturnComment() {
        Sort sort = Sort.by(Sort.Direction.ASC, "created");
        assertEquals(1, commentRepo.findByItemIdIn(Collections.singletonList(createdItem.getId()), sort).size());
    }

    @Test
    public void givenComment_whenFindingByWrongItemIdIn_thenReturnEmptyList() {
        Sort sort = Sort.by(Sort.Direction.ASC, "created");
        assertEquals(0, commentRepo.findByItemIdIn(Collections.singletonList(5L), sort).size());
    }

    private Comment createComment(Long id, User author, Item item) {
        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setText("good");
        comment.setAuthor(author);
        comment.setItem(item);
        return comment;
    }

    private User createUser(Long id) {
        User user = new User();
        user.setName("name" + id);
        user.setEmail("yandex" + id + "@yandex.ru");
        return user;
    }

    private Item createItem(Long id, User owner) {
        Item item = new Item();
        item.setAvailable(true);
        item.setName("name" + id);
        item.setDescription("description" + id);
        item.setOwner(owner);
        return item;
    }
}