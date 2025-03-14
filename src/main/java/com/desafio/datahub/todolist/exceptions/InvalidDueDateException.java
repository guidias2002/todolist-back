package com.desafio.datahub.todolist.exceptions;

public class InvalidDueDateException extends RuntimeException{

    public InvalidDueDateException(String message) {
        super(message);
    }
}
