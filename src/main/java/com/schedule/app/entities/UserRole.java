package com.schedule.app.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author : Thành Lộc
 * @since : 11/4/2022, Fri
 **/
@Entity
@Table(name = "`user_role`")
@Data
@IdClass(UserRole.class)
public class UserRole implements Serializable {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "role_id")
    private String roleId;

//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name="role_id")
//    private Role role;

}
