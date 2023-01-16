package com.schedule.app.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.persistence.EntityManager;

/**
 * @author : Thành Lộc
 * @since : 10/5/2022, Wed
 **/

public class AbstractBaseRepositoryCustom {
    @Autowired
    protected EntityManager entityManager;


    @Autowired
    @Lazy
    protected IUserRoleRepository userRoleRepository;

    @Autowired
    @Lazy
    protected IUserRepository userRepository;


}
