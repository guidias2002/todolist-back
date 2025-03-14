package com.desafio.datahub.todolist.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserPostDto(String name, String email, String password) {}
