package com.schedule.app.repository;

import com.schedule.app.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author : Thành Lộc
 * @since : 11/4/2022, Fri
 **/
public interface IUserRoleRepository extends JpaRepository<UserRole, String> {
    List<UserRole> findUserRolesByRoleId(String roleId);

}
