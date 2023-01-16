package com.schedule.app.services;

import com.schedule.app.entities.UserLogin;

/**
 * @author : Thành Lộc
 * @since : 11/4/2022, Fri
 **/
public interface IUserLoginService {
    void save(UserLogin userLogin);
    UserLogin findUserLoginByUserId(Long id);
}
