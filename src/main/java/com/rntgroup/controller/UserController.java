package com.rntgroup.controller;

import com.rntgroup.facade.BookingFacade;
import com.rntgroup.model.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    BookingFacade bookingFacade;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") long userId) {
        return bookingFacade.getUserById(userId);
    }

    @GetMapping
    public List<User> getUsers(@RequestParam(name = "email", required = false) String email,
                               @RequestParam(name = "name", required = false) String name,
                               @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
                               @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {

        if (Objects.nonNull(email) && Objects.nonNull(name)) {
            throw new RuntimeException("Ошибка в запросе");
        }

        if (Objects.isNull(email) && Objects.isNull(name)) {
            throw new RuntimeException("Метод не реализован");
        }

        if (Objects.nonNull(email)) {
            return List.of(bookingFacade.getUserByEmail(email));
        }

        return bookingFacade.getUsersByName(name, pageSize, pageNum);
    }

    @PostMapping
    public User createUser(User user) {
        return bookingFacade.createUser(user);
    }

    @PatchMapping
    public User updateUser(User user) {
        return bookingFacade.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable("id") long userId) {
        return bookingFacade.deleteUser(userId);
    }

}
