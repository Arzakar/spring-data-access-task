package com.rntgroup.service;

import com.rntgroup.exception.NotFoundException;
import com.rntgroup.model.User;
import com.rntgroup.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    var error = new NotFoundException(String.format("User with id = %s not found", id));
                    log.error("Method {}#findById returned error with message: {}", this.getClass().getSimpleName(), error.getMessage());
                    return error;
                });
    }

    public List<User> findByName(String name, int pageSize, int pageNum) {
        log.debug("Method {}#findByName was called with params: name = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), name, pageSize, pageNum);

        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, Sort.Direction.ASC, "name");
        return userRepository.findByName(name, pageRequest).getContent();
    }

    public User findByEmail(String email) {
        log.debug("Method {}#findByEmail was called with param: email = {}", this.getClass().getSimpleName(), email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    var error = new NotFoundException(String.format("User with email = %s not found", email));
                    log.error("Method {}#findByEmail returned error with message: {}", this.getClass().getSimpleName(), error.getMessage());
                    return error;
                });
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
