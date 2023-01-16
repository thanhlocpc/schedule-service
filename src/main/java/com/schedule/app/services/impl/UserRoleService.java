package com.schedule.app.services.impl;

import com.schedule.app.entities.User;
import com.schedule.app.entities.UserRole;
import com.schedule.app.services.ABaseServices;
import com.schedule.app.services.IUserRoleService;
import org.springframework.stereotype.Service;

/**
 * @author : Thành Lộc
 * @since : 11/6/2022, Sun
 **/
@Service
public class UserRoleService extends ABaseServices implements IUserRoleService {
    @Override
    public UserRole createUserRole(User user, String roleId) {
//        Role role = roleRepository.findById(roleId).get();
//        if (role != null) {
//            UserRole userRole = new UserRole();
//            userRole.setUserId(user.getId());
//            userRole.setRole(role);            UserRole f = userRoleRepository.save(userRole);
//            return f;
//
//        }
        return null;
    }
}
