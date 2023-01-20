package com.schedule.app.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author : Thành Lộc
 * @since : 10/12/2022, Wed
 **/
@Entity
@Table(name = "`token`")
@Data
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String token;



    private Date tokenExpDate;


}
