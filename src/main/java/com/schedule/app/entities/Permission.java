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
@Table(name = "permission")
@Data
public class Permission {

    @Id
    @Column(name = "id")
    private String id;

    private String permissionName;
}
