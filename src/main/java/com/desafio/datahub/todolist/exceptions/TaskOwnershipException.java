package com.desafio.datahub.todolist.exceptions;

public class TaskOwnershipException extends RuntimeException{

    public TaskOwnershipException(String message) {
        super(message);
    }
}
