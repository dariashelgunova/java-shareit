package ru.practicum.shareit.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepo;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class UserServiceIT {
    @Autowired
    private WebApplicationContext applicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;

    @Test
    public void givenNewUser_whenCreatingUser_thenReturnCreatedUser() {
        User user = createUser();
        Long createdUserId = userService.create(user).getId();
        assertTrue(userRepo.findById(createdUserId).isPresent());
    }

    @Test
    public void givenExistingUser_whenUpdatingUser_thenUserIsUpdatedSuccessfully() {
        User user = createUser();
        User existingUser = userRepo.save(user);
        Long existingUserId = existingUser.getId();
        String updatedEmail = "anotheremail@mail.ru";
        existingUser.setEmail(updatedEmail);

        userService.update(existingUser, existingUserId);

        User updatedUser = userRepo.findById(existingUserId).orElseThrow();
        assertEquals(updatedEmail, updatedUser.getEmail());
    }

    @Test
    public void given2ExistingUsers_whenFindingAll_thenReturn2Users() {
        userRepo.saveAll(List.of(
                createUser("name1", "email1@yandex.ru"),
                createUser("name2", "email2@yandex.ru")
        ));

        List<User> users = userService.findAll();

        assertEquals(2, users.size());
    }

    @Test
    public void givenExistingUser_whenFindingById_thenReturnUser() {
        User user = createUser();
        User existingUser = userRepo.save(user);

        User foundUser = userService.findById(existingUser.getId());

        assertEquals(existingUser, foundUser);
    }

    @Test
    public void givenExistingUser_whenDeleteById_thenRepoIsEmpty() {
        User user = createUser();
        User existingUser = userRepo.save(user);

        userService.deleteById(existingUser.getId());

        assertTrue(userService.findAll().isEmpty());
    }

    @Test
    public void given2ExistingUsers_whenDeletingAll_thenRepoIsEmpty() {
        userRepo.saveAll(List.of(
                createUser("name1", "email1@yandex.ru"),
                createUser("name2", "email2@yandex.ru")
        ));

        userService.deleteAll();

        assertTrue(userService.findAll().isEmpty());
    }

    private User createUser() {
        User user = new User();
        user.setName("name1");
        user.setEmail("yandex1@yandex.ru");
        return user;
    }

    private User createUser(String name, String email) {
        return new User(null, email, name);
    }

}
