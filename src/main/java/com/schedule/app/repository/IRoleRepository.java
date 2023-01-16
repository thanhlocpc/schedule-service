package com.schedule.app.repository;

import com.schedule.app.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : Thành Lộc
 * @since : 11/6/2022, Sun
 **/
public interface IRoleRepository extends JpaRepository<Role, String> {
}
