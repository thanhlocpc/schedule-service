package com.schedule.app.models.dtos.auths;

import com.schedule.app.entities.Role;
import com.schedule.app.entities.UserLogin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author ThanhLoc
 * @created 2/2/2023
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {

    private Long id;

    private String name;

    private String email;

    private String gender;

    private String phone;

    private Date birthday;

    private String username;

    private Set<Role> roles;

    private String introduce;

    private String urlAvt;

    private Date createdAt;
}
