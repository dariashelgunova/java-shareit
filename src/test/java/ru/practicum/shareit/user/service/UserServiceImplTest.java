package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundObjectException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.db.UserRepo;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl service;
    @Mock
    UserRepo userRepo;

    @Test
    public void givenUsers_whenFindingAllUsers_thenInvokeUserRepo() {
        when(userRepo.findAll()).thenReturn(Collections.emptyList());

        service.findAll();

        verify(userRepo, times(1)).findAll();
    }

    @Test
    public void givenUser_whenCreatingUser_thenInvokeUserRepo() {
        User user = createUserWithId(1L);
        when(userRepo.save(user)).thenReturn(user);

        service.create(user);

        verify(userRepo, times(1)).save(user);
    }

    @Test
    public void givenNewUser_whenIUserIsNotFound_thenThrowNotFoundObjectException() {
        User user = createUserWithId(1L);
        when(userRepo.findById(1L)).thenThrow(NotFoundObjectException.class);

        assertThrows(NotFoundObjectException.class, () -> service.update(user, 1L));
    }

    @Test
    public void givenUpdatedUser_whenUserIsUpdated_returnUpdatedUser() {
        long userId = 1L;
        User user = createUserWithId(userId);
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        User result = service.update(user, userId);

        assertNotNull(result);
    }

    @Test
    public void givenUpdatedUserWithName_whenUpdatingUser_returnUserWithUpdatedName() {
        String userName = "UpdateName";
        long userId = 1L;
        User updatedUser = createUserWithId(userId);
        updatedUser.setName(userName);
        when(userRepo.findById(userId)).thenReturn(Optional.of(updatedUser));

        User result = service.update(updatedUser, userId);

        assertEquals(userName, result.getName());
    }

    @Test
    public void givenUpdatedUserWithEmail_whenUpdatingUser_returnUserWithUpdatedEmail() {
        String userEmail = "Update@com.com";
        long userId = 1L;
        User updatedUser = createUserWithId(userId);
        updatedUser.setEmail(userEmail);
        when(userRepo.findById(userId)).thenReturn(Optional.of(updatedUser));

        User result = service.update(updatedUser, userId);

        assertEquals(userEmail, result.getEmail());
    }

    @Test
    public void givenUserId_whenNoUserWithIdPresent_throwNotFoundObjectException() {
        long userId = 1L;
        when(userRepo.findById(userId)).thenThrow(NotFoundObjectException.class);

        assertThrows(NotFoundObjectException.class, () -> service.findById(userId));
    }

    @Test
    public void givenUserId_whenUserWithIdPresent_returnUserWithGivenId() {
        long userId = 1L;
        when(userRepo.findById(userId)).thenReturn(Optional.of(createUserWithId(userId)));

        User foundItem = service.findById(userId);

        assertEquals(userId, foundItem.getId());
    }

    @Test
    public void givenUserId_whenDeletingUserById_thenInvokeUserRepo() {
        long userId = 1L;
        doNothing().when(userRepo).deleteById(userId);

        service.deleteById(userId);

        verify(userRepo, times(1)).deleteById(userId);
    }

    @Test
    public void givenUserId_whenDeletingAllUsers_thenInvokeUserRepo() {
        doNothing().when(userRepo).deleteAll();

        service.deleteAll();

        verify(userRepo, times(1)).deleteAll();
    }

    private User createUserWithId(long id) {
        User user = new User();
        user.setId(id);
        user.setName("user" + id);
        user.setEmail("user" + id + "@user.com");
        return user;
    }

}