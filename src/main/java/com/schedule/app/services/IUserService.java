package com.schedule.app.services;

import com.schedule.app.entities.User;

import java.util.List;

/**
 * @author : Thành Lộc
 * @since : 10/12/2022, Wed
 **/

public interface IUserService {
    User createUser(User user);

    User findByEmail(String username);

    User findById(Long userId);

    List<User> findUsersByIdIn(List<Long> ids);

}
