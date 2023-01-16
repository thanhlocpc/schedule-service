package com.schedule.app.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : Thành Lộc
 * @since : 11/4/2022, Fri
 **/
@Entity
@Table(name = "`role`")
@Data
public class Role{

    @Id
    @Column(name = "id")
    private String id;

    private String roleName;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission", joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private Set<Permission> permissions = new HashSet<>();
}
