package com.schedule.app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author : Thành Lộc
 * @since : 02/08/2022
 **/
@Entity
@Table(name = "`otp`")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Otp {

    @Id
    @Column(name = "user_id")
    private Long userId;

    private Long otp;

    private Date createdAt;
}
