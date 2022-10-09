package com.rntgroup.controller;

import com.rntgroup.dto.UserDto;
import com.rntgroup.exception.BadRequestException;
import com.rntgroup.facade.BookingFacade;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    BookingFacade bookingFacade;

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        return bookingFacade.createUser(userDto);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") UUID userId) {
        return bookingFacade.getUserById(userId);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable("id") UUID userId,
                              @RequestBody UserDto userDto) {
        return bookingFacade.updateUser(userDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable("id") UUID userId) {
        return bookingFacade.deleteUser(userId);
    }

    @GetMapping("/search")
    public List<UserDto> getUsers(@RequestParam(name = "email", required = false) String email,
                               @RequestParam(name = "name", required = false) String name,
                               @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
                               @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
                               @RequestParam(name = "direction", required = false, defaultValue = "ASC") Sort.Direction direction,
                               @RequestParam(name = "sortParam", required = false, defaultValue = "id") String sortParam) {

        boolean emailIsNull = Objects.isNull(email);
        boolean nameIsNull = Objects.isNull(name);

        List<Boolean> requestParamsNullableFlags = List.of(
                emailIsNull,
                nameIsNull
        );

        long notNullParamsCount = requestParamsNullableFlags.stream()
                .filter(value -> value.equals(Boolean.FALSE))
                .count();

        if (notNullParamsCount != 1) {
            throw new BadRequestException("Ошибка в запросе: указано неверное количество параметров поиска");
        }

        if (!emailIsNull) {
            return List.of(bookingFacade.getUserByEmail(email));
        }

        return bookingFacade.getUsersByName(name, pageNum, pageSize, direction, sortParam);
    }

}
