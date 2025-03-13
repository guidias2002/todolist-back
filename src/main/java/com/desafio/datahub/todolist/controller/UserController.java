package com.desafio.datahub.todolist.controller;

import com.desafio.datahub.todolist.dto.UserDto;
import com.desafio.datahub.todolist.dto.UserPostDto;
import com.desafio.datahub.todolist.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserPostDto userPostDto) {

        return new ResponseEntity<>(userService.createUser(userPostDto), HttpStatus.CREATED);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<UserDto>> findAllUsers() {

        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }
}
