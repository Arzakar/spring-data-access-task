package com.rntgroup.service;

import com.rntgroup.exception.NotFoundException;
import com.rntgroup.model.User;
import com.rntgroup.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;

    public User create(User user) {
        log.debug("Method {}#create was called with param: user = {}", this.getClass().getSimpleName(), user);

        return userRepository.save(user);
    }

    public User findById(UUID id) {
        log.debug("Method {}#findById was called with param: id = {}", this.getClass().getSimpleName(), id);

        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("User with id = %s not found", id)));
    }

    public List<User> findByName(String name, Pageable pageable) {
        log.debug("Method {}#findByName was called with params: name = {}, pageable = {}",
                this.getClass().getSimpleName(), name, pageable);

        return userRepository.findByName(name, pageable).getContent();
    }

    public User findByEmail(String email) {
        log.debug("Method {}#findByEmail was called with param: email = {}", this.getClass().getSimpleName(), email);

        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(String.format("User with email = %s not found", email)));
    }

    public User update(User user) {
        log.debug("Method {}#update was called with param: user = {}", this.getClass().getSimpleName(), user);

        return userRepository.save(user);
    }

    public boolean deleteById(UUID id) {
        log.debug("Method {}#deleteById was called with param: id = {}", this.getClass().getSimpleName(), id);

        userRepository.deleteById(id);
        return !userRepository.existsById(id);
    }
}
