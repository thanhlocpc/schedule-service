package com.schedule.app.entities;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author : Thành Lộc
 * @since : 11/4/2022, Fri
 **/
@Entity
@Table(name = "`user`")
@Data
public class User extends BaseEntity {

    private String name;

    private String email;

    private String gender;

    private String phone;

    private Date birthday;

    private String level;

    private String username;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    private UserLogin userLogin;
    private String introduce;

    private String urlAvt;

    private Date createdAt;




}
