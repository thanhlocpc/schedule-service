package com.schedule.app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "refresh_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken extends BaseEntity{

    @OneToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private User user;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    @Column(nullable = false)
    private Date expiryDate;
}
