package com.desafio.datahub.todolist.service;

import com.desafio.datahub.todolist.domain.TaskEntity;
import com.desafio.datahub.todolist.dto.TaskDto;
import com.desafio.datahub.todolist.dto.TaskPostDto;
import com.desafio.datahub.todolist.enums.TaskStatus;
import com.desafio.datahub.todolist.exceptions.InvalidDueDateException;
import com.desafio.datahub.todolist.exceptions.NotFoundException;
import com.desafio.datahub.todolist.exceptions.TaskOwnershipException;
import com.desafio.datahub.todolist.mapper.TaskMapper;
import com.desafio.datahub.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskMapper taskMapper;


    public TaskDto createTask(TaskPostDto taskPostDto, Long userId) {
        userService.findUserById(userId);

        if(taskPostDto.dueDate().isBefore(LocalDate.now())) {
            throw new InvalidDueDateException("A data de vencimento não pode ser anterior à data atual.");
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

    public void deleteTaskById(Long taskId, Long userId) {
        userService.findUserById(userId);

        TaskEntity task = findTaskById(taskId);

        if(!task.getUserId().equals(userId)) {
            throw new TaskOwnershipException("Apenas o criador da tarefa pode exclui-lá.");
        }

        taskRepository.deleteById(taskId);
    }

    public List<TaskDto> filterTaskByStatus(TaskStatus status) {

        return taskRepository.findTasksByStatus(status)
                .stream()
                .sorted(Comparator.comparing(TaskEntity::getDueDate))
                .map(taskMapper::toTaskDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> orderByDueDate() {

        return taskRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(TaskEntity::getDueDate))
                .map(taskMapper::toTaskDto)
                .collect(Collectors.toList());
    }

    public TaskEntity findTaskById(Long taskId) {

        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Tarefa não encontrada."));
    }
}
