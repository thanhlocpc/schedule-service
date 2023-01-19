package com.schedule.app.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author : Thành Lộc
 * @since : 11/4/2022, Fri
 **/

@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class  BaseEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

//    private boolean deleted = false;
//
//    @CreatedDate
//    private Date createdAt;
//
//    @LastModifiedDate
//    private Date updatedAt;
//
//    private Long createdBy;
//
//    private Long updatedBy;
}
