package com.schedule.app.converter;

import com.schedule.app.entities.User;
import com.schedule.app.models.dtos.auths.UserDTO;
import org.modelmapper.ModelMapper;

/**
 * @author ThanhLoc
 * @created 2/2/2023
 */
public class UserConverter {

    public static UserDTO toUserDTO(User user){
        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }
}
