package com.desafio.datahub.todolist.service;

import com.desafio.datahub.todolist.domain.TaskEntity;
import com.desafio.datahub.todolist.dto.TaskDto;
import com.desafio.datahub.todolist.dto.TaskPostDto;
import com.desafio.datahub.todolist.dto.TaskUpdateDto;
import com.desafio.datahub.todolist.enums.TaskStatus;
import com.desafio.datahub.todolist.exceptions.BlankFieldException;
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
        validateTaskPostDto(taskPostDto);
        userService.findUserById(userId);

        TaskEntity newTask = taskMapper.toTaskEntity(taskPostDto);
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

        validateTaskUserPermission(userId, task.getUserId());

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

    public TaskDto updateTask(Long taskId, Long userId, TaskUpdateDto taskUpdateDto) {
        userService.findUserById(userId);

        TaskEntity task = findTaskById(taskId);

        updateTaskFields(task, taskUpdateDto, userId);

        taskRepository.save(task);

        return taskMapper.toTaskDto(task);
    }

    private void updateTaskFields(TaskEntity task, TaskUpdateDto taskUpdateDto, Long userId) {
        if (taskUpdateDto.title() != null && !taskUpdateDto.title().isBlank()) {
            task.setTitle(taskUpdateDto.title());
        }

        if (taskUpdateDto.description() != null && !taskUpdateDto.description().isBlank()) {
            task.setDescription(taskUpdateDto.description());
        }

        if (taskUpdateDto.dueDate() != null) {
            validateDueDate(taskUpdateDto.dueDate());
            task.setDueDate(taskUpdateDto.dueDate());
        }

        if (taskUpdateDto.status() != null) {
            task.setStatus(taskUpdateDto.status());
        }

        validateTaskUserPermission(userId, task.getUserId());
    }

    public void validateTaskUserPermission(Long userId, Long taskUserId) {
        if(!taskUserId.equals(userId)) {
            throw new TaskOwnershipException("Apenas o criador da tarefa pode editá-la ou excluí-la.");
        }
    }

    private void validateTaskPostDto(TaskPostDto taskPostDto) {
        if (taskPostDto.title() == null || taskPostDto.title().isBlank()) {
            throw new BlankFieldException("O campo 'title' não pode estar vazio.");
        }

        if (taskPostDto.description() == null || taskPostDto.description().isBlank()) {
            throw new BlankFieldException("O campo 'description' não pode estar vazio.");
        }

        if (taskPostDto.dueDate() == null) {
            throw new BlankFieldException("O campo 'dueDate' não pode estar vazio.");
        }

        if (taskPostDto.status() == null) {
            throw new BlankFieldException("O campo 'status' não pode estar vazio.");
        }

        validateDueDate(taskPostDto.dueDate());
    }

    public TaskEntity findTaskById(Long taskId) {

        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Tarefa não encontrada."));
    }

    private void validateDueDate(LocalDate dueDate) {
        if(dueDate.isBefore(LocalDate.now())) {
            throw new InvalidDueDateException("A data de vencimento não pode ser anterior à data atual.");
        }
    }
}
