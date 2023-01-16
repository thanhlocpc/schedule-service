package com.schedule.app.services.impl;

import com.schedule.app.entities.UserLogin;
import com.schedule.app.services.ABaseServices;
import com.schedule.app.services.IUserLoginService;
import org.springframework.stereotype.Service;

/**
 * @author : Thành Lộc
 * @since : 11/4/2022, Fri
 **/
@Service
public class UserLoginService extends ABaseServices implements IUserLoginService {
    @Override
    public void save(UserLogin userLogin) {
        userLoginRepository.save(userLogin);
    }

    @Override
    public UserLogin findUserLoginByUserId(Long id) {
        return userLoginRepository.findUserLoginByUserId(id);
    }
}
