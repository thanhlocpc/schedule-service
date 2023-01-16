package com.schedule.app.repository;

import com.schedule.app.entities.User;

/**
 * @author : Thành Lộc
 * @since : 10/5/2022, Wed
 **/
public interface IRepositoryUserCustom {
    User findUserById(Long userId);
}
