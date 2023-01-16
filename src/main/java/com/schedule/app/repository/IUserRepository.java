package com.schedule.app.repository;

import com.schedule.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author : Thành Lộc
 * @since : 10/12/2022, Wed
 **/
public interface IUserRepository extends JpaRepository<User, Long>, IRepositoryUserCustom {
    User findByUsername(String username);
    User findByEmail(String email);
    Optional<User> findById(Long userId);
    List<User> findUsersByIdIn(List<Long> ids);
}
