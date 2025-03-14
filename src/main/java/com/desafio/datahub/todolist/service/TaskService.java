package com.desafio.datahub.todolist.service;

import com.desafio.datahub.todolist.domain.TaskEntity;
import com.desafio.datahub.todolist.dto.TaskDto;
import com.desafio.datahub.todolist.dto.TaskPostDto;
import com.desafio.datahub.todolist.mapper.TaskMapper;
import com.desafio.datahub.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskMapper taskMapper;


    public TaskDto createTask(TaskPostDto taskPostDto, Long userId) {
        if(!userService.existsUserById(userId)) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        if(taskPostDto.dueDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("A data de vencimento não pode ser anterior à data atual.");
        }

        TaskEntity newTask = new TaskEntity();

        newTask.setTitle(taskPostDto.title());
        newTask.setDescription(taskPostDto.description());
        newTask.setDueDate(taskPostDto.dueDate());
        newTask.setStatus(taskPostDto.status());
        newTask.setUserId(userId);

        taskRepository.save(newTask);

        return taskMapper.toTaskDto(newTask);
    }

    public List<TaskDto> findAllTasks() {

        return taskMapper.toTaskDtoList(taskRepository.findAll());
    }
}
