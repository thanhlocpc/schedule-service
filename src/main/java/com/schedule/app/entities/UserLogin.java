package com.schedule.app.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : Thành Lộc
 * @since : 11/4/2022, Fri
 **/

@Entity
@Table(name = "`user_login`")
@Data
public class UserLogin {

    @Id
    @Column(name = "user_id")
    private Long userId;

    private String password;

    @Column(name = "active")
    private Boolean active;


}
