package com.nesreading.mapper;

import com.nesreading.domain.User;
import com.nesreading.dto.RegisterDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User registerDtoToUser(RegisterDTO registerDTO) {
        User user = new User(
                "USER", registerDTO.getFirstName(),
                registerDTO.getLastName(), registerDTO.getEmail(), ""
        );
        user.setPassword(registerDTO.getPassword());

        return user;
    }
}
