package com.geko.ecommerce.DTO.User;

import com.geko.ecommerce.Entity.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getEmail()
        );
    }

    public static User toUser(UserDTO userDTO) {
        return User.builder()
                .username(userDTO.getName())
                .email(userDTO.getEmail())
                .build();
    }
}
