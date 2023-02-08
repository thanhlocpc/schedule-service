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
}
