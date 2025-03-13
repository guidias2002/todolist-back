package com.desafio.datahub.todolist.service;

import com.desafio.datahub.todolist.dto.UserDto;
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

    public void createUser(UserDto userDto) {

        if(userRepository.existsByEmail(userDto.email())) {
            throw new RuntimeException("Email já cadastrado");
        }

        userRepository.save(userMapper.toUserEntity(userDto));
    }

    public List<UserDto> findAllUsers() {

        return userMapper.toUserDtoList(userRepository.findAll());
    }
}
