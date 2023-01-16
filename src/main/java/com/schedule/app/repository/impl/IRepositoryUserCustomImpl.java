package com.schedule.app.repository.impl;


import com.schedule.app.entities.User;
import com.schedule.app.repository.AbstractBaseRepositoryCustom;
import com.schedule.app.repository.IRepositoryUserCustom;

import javax.persistence.Query;
import java.util.List;

/**
 * @author : Thành Lộc
 * @since : 10/5/2022, Wed
 **/
public class IRepositoryUserCustomImpl extends AbstractBaseRepositoryCustom implements IRepositoryUserCustom {

    @Override
    public User findUserById(Long userId) {
        Query query =  entityManager.createQuery("select u from User u where u.id =:userId ");
        query.setParameter("userId",userId);
        List<User> s = query.getResultList();

        return  null;
    }
}
