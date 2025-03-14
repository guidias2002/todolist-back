package com.desafio.datahub.todolist.service;

import com.desafio.datahub.todolist.domain.UserEntity;
import com.desafio.datahub.todolist.dto.UserDto;
import com.desafio.datahub.todolist.dto.UserPostDto;
import com.desafio.datahub.todolist.mapper.UserMapper;
import com.desafio.datahub.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserDto createUser(UserPostDto userPostDto) {

        if(userRepository.existsByEmail(userPostDto.email())) {
            throw new RuntimeException("Email já cadastrado");
        }

        UserEntity newUser = userRepository.save(userMapper.toUserEntity(userPostDto));

        return userMapper.toUserDto(newUser);
    }

    public List<UserDto> findAllUsers() {

        return userMapper.toUserDtoList(userRepository.findAll());
    }

    public boolean existsUserById(Long userId) {

        return userRepository.existsById(userId);
    }
}
