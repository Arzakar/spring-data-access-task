package com.rntgroup.service.implementation;

import com.rntgroup.model.User;
import com.rntgroup.repository.UserRepository;
import com.rntgroup.repository.util.Page;
import com.rntgroup.service.UserService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class.getSimpleName());

    UserRepository userRepository;

    @Override
    public User create(User user) {
        LOG.debug("Method {}#create was called with param: user = {}", this.getClass().getSimpleName(), user);
        return userRepository.save(user);
    }

    @Override
    public User findById(long id) {
        LOG.debug("Method {}#findById was called with param: id = {}", this.getClass().getSimpleName(), id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    var error = new RuntimeException(String.format("User with id = %d not found", id));
                    LOG.error("Method {}#findById returned error with message: {}", this.getClass().getSimpleName(), error.getMessage());
                    return error;
                });
    }

    @Override
    public List<User> findByName(String name, int pageSize, int pageNum) {
        LOG.debug("Method {}#findByName was called with params: name = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), name, pageSize, pageNum);
        return userRepository.findByName(name, Page.of(pageSize, pageNum))
                .getContent();
    }

    @Override
    public User findByEmail(String email) {
        LOG.debug("Method {}#findByEmail was called with param: email = {}", this.getClass().getSimpleName(), email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    var error = new RuntimeException(String.format("User with email = %s not found", email));
                    LOG.error("Method {}#findByEmail returned error with message: {}", this.getClass().getSimpleName(), error.getMessage());
                    return error;
                });
    }

    @Override
    public User update(User user) {
        LOG.debug("Method {}#update was called with param: user = {}", this.getClass().getSimpleName(), user);
        return userRepository.update(user);
    }

    @Override
    public User deleteById(long id) {
        LOG.debug("Method {}#deleteById was called with param: id = {}", this.getClass().getSimpleName(), id);
        return userRepository.deleteById(id);
    }
}
