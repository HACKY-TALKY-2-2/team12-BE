package com.hackytalky.team12server.service;

import com.hackytalky.team12server.dto.ReviewDto;
import com.hackytalky.team12server.dto.UserDto;
import com.hackytalky.team12server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Long register(UserDto.RegisterRequest registerRequest) {
        return userRepository.save(registerRequest.toUserEntity()).getUserId();
    }


}
