package com.desafio.datahub.todolist.dto;

import com.desafio.datahub.todolist.enums.TaskStatus;

import java.time.LocalDate;

public record TaskDto(Long id, String title, String description, TaskStatus status, LocalDate dueDate, Long userId) {
}
