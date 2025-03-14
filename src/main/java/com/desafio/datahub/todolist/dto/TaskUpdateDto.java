package com.desafio.datahub.todolist.dto;

import com.desafio.datahub.todolist.enums.TaskStatus;

import java.time.LocalDate;

public record TaskUpdateDto(String title, String description, LocalDate dueDate, TaskStatus status) {
}
