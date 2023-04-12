package ru.practicum.shareit.request.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.db.UserRepo;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestRepoTest {

    @Autowired
    private ItemRequestRepo repo;
    @Autowired
    private UserRepo userRepo;
    private ItemRequest createdRequest;


    @Test
    public void givenRequest_whenFindingByRequestorId_thenReturnRequest() {
        User requestAuthor = createUser(2L);
        User requestor = userRepo.save(requestAuthor);

        ItemRequest itemRequest = createItemRequest(requestor);
        createdRequest = repo.save(itemRequest);

        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        assertEquals(1, repo.findByRequestorId(requestor.getId(), sort).size());
    }

    @Test
    public void givenRequest_whenFindingByRequestorIdNot_thenReturnEmpty() {
        User requestAuthor = createUser(2L);
        User requestor = userRepo.save(requestAuthor);

        ItemRequest itemRequest = createItemRequest(requestor);
        createdRequest = repo.save(itemRequest);

        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        assertEquals(0, repo.findByRequestorIdNot(requestor.getId(), sort).size());
    }

    private User createUser(Long id) {
        User user = new User();
        user.setName("name" + id);
        user.setEmail("yandex" + id + "@yandex.ru");
        return user;
    }

    private ItemRequest createItemRequest(User requestor) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("description");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(requestor);

        return  itemRequest;
    }

}