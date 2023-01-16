package com.schedule.app.services.impl;

import com.schedule.app.entities.User;
import com.schedule.app.services.ABaseServices;
import com.schedule.app.services.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author : Thành Lộc
 * @since : 10/12/2022, Wed
 **/
@Service
public class UserService extends ABaseServices implements IUserService {
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);

    }

    @Override
    public User findById(Long userId) {
        Optional<User> u = userRepository.findById(userId);
        return u.isPresent() == false ? null : u.get();
    }

    @Override
    public List<User> findUsersByIdIn(List<Long> ids) {
        return userRepository.findUsersByIdIn(ids);
    }
}
