package com.rntgroup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rntgroup.exception.NotFoundException;
import com.rntgroup.model.User;
import com.rntgroup.repository.UserRepository;

import com.rntgroup.repository.util.Page;
import com.rntgroup.repository.util.SearchResult;
import com.rntgroup.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Должен сохранить User в БД и вернуть сохранённый объект")
    void shouldCreateEvent() {
        User testUser = new User(0L, "TestUser", "test.email@test.com");

        when(userRepository.save(any(User.class))).thenReturn(testUser);

        assertEquals(testUser, userService.create(testUser));
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Должен вернуть User из БД, найденного по id")
    void shouldReturnUserById() {
        User testUser = new User(0L, "TestUser", "test.email@test.com");

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(testUser));

        assertEquals(testUser, userService.findById(0L));
        verify(userRepository).findById(0L);
    }

    @Test
    @DisplayName("Должен выбросить исключение, т.к. User с заданным id не найден")
    void shouldThrowExceptionBecauseUserByIdNotFound() {
        NotFoundException expectedException = new NotFoundException("User with id = 0 not found");

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> userService.findById(0L));
        verify(userRepository).findById(0L);
        assertEquals(expectedException.getMessage(), thrown.getMessage());
    }

    @Test
    @DisplayName("Должен вернуть список, содержащий Users с одинаковыми именами с определённой страницы")
    void shouldReturnUsersByName() {
        List<User> testUsers = List.of(new User(0L, "TestUser", "test_01.email@test.com"),
                new User(1L, "TestUser", "test_02.email@test.com"));

        when(userRepository.findByName(any(String.class), any(Page.class)))
                .thenReturn(new SearchResult<User>().setContent(testUsers).setPage(Page.of(2, 1)));

        assertEquals(testUsers, userService.findByName("TestUser", 2, 1));
        verify(userRepository).findByName("TestUser", Page.of(2, 1));
    }

    @Test
    @DisplayName("Должен вернуть User из БД, найденного по email")
    void shouldReturnUserByEmail() {
        User testUser = new User(0L, "TestUser", "test.email@test.com");

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(testUser));

        assertEquals(testUser, userService.findByEmail("test.email@test.com"));
        verify(userRepository).findByEmail("test.email@test.com");
    }

    @Test
    @DisplayName("Должен выбросить исключение, т.к. User с заданным email не найден")
    void shouldThrowExceptionBecauseUserByEmailNotFound() {
        NotFoundException expectedException = new NotFoundException("User with email = incorrect.email@com not found");

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> userService.findByEmail("incorrect.email@com"));
        verify(userRepository).findByEmail("incorrect.email@com");
        assertEquals(expectedException.getMessage(), thrown.getMessage());
    }

    @Test
    @DisplayName("Должен обновить User в БД и вернуть обновлённый объект")
    void shouldUpdateEvent() {
        User testUser = new User(2L, "UpdatedTestUser", "test.email@test.com");

        when(userRepository.update(any(User.class))).thenReturn(testUser);

        assertEquals(testUser, userService.update(testUser));
        verify(userRepository).update(testUser);
    }

    @Test
    @DisplayName("Должен удалить User из БД и вернуть удалённый объект")
    void shouldDeleteEventById() {
        User testUser = new User(0L, "DeletedTestUser", "test.email@test.com");

        when(userRepository.deleteById(any(Long.class))).thenReturn(testUser);

        assertEquals(testUser, userService.deleteById(0L));
        verify(userRepository).deleteById(0L);
    }
}