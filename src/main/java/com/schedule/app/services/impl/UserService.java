package com.schedule.app.services.impl;

import com.schedule.app.entities.User;
import com.schedule.app.entities.UserLogin;
import com.schedule.app.handler.TutorServiceException;
import com.schedule.app.security.UserPrincipal;
import com.schedule.app.services.ABaseServices;
import com.schedule.app.services.IUserService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Override
    public UserPrincipal getUserPrincipal(User userNet) {
        if (userNet == null || userNet.getUserLogin() == null) {
            throw new TutorServiceException("Không tìm thấy tài khoản");
        }

        UserLogin userLogin = userNet.getUserLogin();

        if (!userLogin.getActive()) {
            throw new TutorServiceException("Tài khoản chưa được kích hoạt");
        }

        UserPrincipal userPrincipal = new UserPrincipal();
        if (null != userNet) {
            userPrincipal.setUserId(userNet.getId());
            userPrincipal.setUsername(userNet.getEmail());
            userPrincipal.setPassword(userLogin.getPassword());

            // lấy role
            Set<String> authorities = new HashSet<>();
            if (null != userNet.getRoles()) {
                userNet.getRoles().forEach(r -> {
                    authorities.add(r.getId());
                    r.getPermissions().forEach(p -> authorities.add(p.getId()));
                });
            }
            userPrincipal.setAuthorities(authorities);
        }
        return userPrincipal;
    }
}
