package com.desafio.datahub.todolist.service;

import com.desafio.datahub.todolist.domain.UserEntity;
import com.desafio.datahub.todolist.dto.LoginResponseDto;
import com.desafio.datahub.todolist.dto.UserDto;
import com.desafio.datahub.todolist.dto.UserPostDto;
import com.desafio.datahub.todolist.exceptions.BlankFieldException;
import com.desafio.datahub.todolist.exceptions.EmailAlreadyExistsException;
import com.desafio.datahub.todolist.exceptions.InvalidPasswordException;
import com.desafio.datahub.todolist.exceptions.NotFoundException;
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
        validateUserPostDto(userPostDto);

        if(userRepository.existsByEmail(userPostDto.email())) {
            throw new EmailAlreadyExistsException("Email '" + userPostDto.email() + "' já cadastrado");
        }

        UserEntity newUser = userRepository.save(userMapper.toUserEntity(userPostDto));

        return userMapper.toUserDto(newUser);
    }

    public LoginResponseDto loginUser(String email, String password) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email não encontrado."));

        if(!user.getPassword().equals(password)) {
            throw new InvalidPasswordException("Senha inválida.");
        }

        return new LoginResponseDto(user.getId());
    }

    public List<UserDto> findAllUsers() {

        return userMapper.toUserDtoList(userRepository.findAll());
    }

    public UserEntity findUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }

    private void validateUserPostDto(UserPostDto userPostDto) {
        if(userPostDto.name() == null || userPostDto.name().isBlank()) {
            throw new BlankFieldException("O campo 'name' não pode estar vazio.");
        }

        if(userPostDto.email() == null || userPostDto.email().isBlank()) {
            throw new BlankFieldException("O campo 'email' não pode estar vazio.");
        }

        if(userPostDto.password() == null || userPostDto.password().isBlank()) {
            throw new BlankFieldException("O campo 'password' não pode estar vazio.");
        }
    }
}
