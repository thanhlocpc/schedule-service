package com.schedule.app.services;

import com.schedule.app.entities.User;
import com.schedule.app.entities.UserRole;

/**
 * @author : Thành Lộc
 * @since : 11/6/2022, Sun
 **/
public interface IUserRoleService {
    UserRole createUserRole(User user, String roleId);
}
