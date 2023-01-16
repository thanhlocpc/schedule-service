package com.schedule.app.repository;

import com.schedule.app.entities.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : Thành Lộc
 * @since : 11/4/2022, Fri
 **/
public interface IUserLoginRepository extends JpaRepository<UserLogin, Long> {
    UserLogin findUserLoginByUserId(Long id);

}
