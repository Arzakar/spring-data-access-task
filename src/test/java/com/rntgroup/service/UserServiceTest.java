package com.rntgroup.service;

import static com.rntgroup.TestDataUtil.DEFAULT_PAGE_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rntgroup.TestDataUtil;
import com.rntgroup.exception.NotFoundException;
import com.rntgroup.model.User;
import com.rntgroup.repository.UserRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Должен сохранить User в БД и вернуть сохранённый объект")
    void shouldCreateEvent() {
        User user = TestDataUtil.getRandomUser();

        when(userRepository.save(any(User.class))).thenReturn(user);

        assertEquals(user, userService.create(user));
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Должен вернуть User из БД, найденного по id")
    void shouldReturnUserById() {
        UUID id = UUID.randomUUID();
        User user = TestDataUtil.getRandomUser().setId(id);

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        assertEquals(user, userService.findById(id));
        verify(userRepository).findById(id);
    }

    @Test
    @DisplayName("Должен выбросить исключение, т.к. User с заданным id не найден")
    void shouldThrowExceptionBecauseUserByIdNotFound() {
        UUID id = UUID.randomUUID();
        NotFoundException expectedException = new NotFoundException(String.format("User with id = %s not found", id));

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> userService.findById(id));
        verify(userRepository).findById(id);
        assertEquals(expectedException.getMessage(), thrown.getMessage());
    }

    @Test
    @DisplayName("Должен вернуть список, содержащий Users с одинаковыми именами с определённой страницы")
    void shouldReturnUsersByName() {
        String name = "UserName";
        List<User> users = List.of(
                new User().setName(name),
                new User().setName(name)
        );

        when(userRepository.findByName(any(String.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(users, DEFAULT_PAGE_REQUEST, users.size()));

        assertEquals(users, userService.findByName(name, DEFAULT_PAGE_REQUEST));
        verify(userRepository).findByName(name, DEFAULT_PAGE_REQUEST);
    }

    @Test
    @DisplayName("Должен вернуть User из БД, найденного по email")
    void shouldReturnUserByEmail() {
        String email = "mail@mail.com";
        User user = new User().setEmail(email);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        assertEquals(user, userService.findByEmail(email));
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Должен выбросить исключение, т.к. User с заданным email не найден")
    void shouldThrowExceptionBecauseUserByEmailNotFound() {
        String email = "mail@mail.com";
        NotFoundException expectedException = new NotFoundException(String.format("User with email = %s not found", email));

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> userService.findByEmail(email));
        verify(userRepository).findByEmail(email);
        assertEquals(expectedException.getMessage(), thrown.getMessage());
    }

    @Test
    @DisplayName("Должен обновить User в БД и вернуть обновлённый объект")
    void shouldUpdateEvent() {
        User user = TestDataUtil.getRandomUser();

        when(userRepository.save(any(User.class))).thenReturn(user);

        assertEquals(user, userService.update(user));
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Должен удалить User из БД и вернуть удалённый объект")
    void shouldDeleteEventById() {
        UUID id = UUID.randomUUID();

        when(userRepository.existsById(any(UUID.class))).thenReturn(false);

        assertTrue(userService.deleteById(id));
        verify(userRepository).deleteById(id);
    }
}