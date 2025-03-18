package com.desafio.datahub.todolist.service;

import com.desafio.datahub.todolist.domain.TaskEntity;
import com.desafio.datahub.todolist.domain.UserEntity;
import com.desafio.datahub.todolist.dto.TaskDto;
import com.desafio.datahub.todolist.dto.TaskPostDto;
import com.desafio.datahub.todolist.enums.TaskStatus;
import com.desafio.datahub.todolist.mapper.TaskMapper;
import com.desafio.datahub.todolist.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private TaskPostDto taskPostDto;
    private TaskEntity taskEntity;
    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        taskPostDto = new TaskPostDto(
                "Nova Tarefa",
                "Descrição da tarefa",
                LocalDate.of(2025, 3, 20),
                TaskStatus.PENDENTE
        );

        taskEntity = new TaskEntity(1L,
                taskPostDto.title(),
                taskPostDto.description(),
                taskPostDto.status(),
                taskPostDto.dueDate(),
                1L
        );

        taskDto = new TaskDto(
                taskEntity.getId(),
                taskEntity.getTitle(),
                taskEntity.getDescription(),
                taskEntity.getStatus(),
                taskEntity.getDueDate(),
                taskEntity.getUserId()
        );
    }

    @Test
    void testCreateTask_Success() {
        Long userId = 1L;

        when(taskMapper.toTaskEntity(taskPostDto)).thenReturn(taskEntity);
        when(taskMapper.toTaskDto(taskEntity)).thenReturn(taskDto);
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);

        TaskDto result = taskService.createTask(taskPostDto, userId);

        assertNotNull(result);
        assertEquals(taskPostDto.title(), result.title());
        assertEquals(taskPostDto.description(), result.description());
        assertEquals(taskPostDto.dueDate(), result.dueDate());
        assertEquals(taskPostDto.status(), result.status());

        verify(userService, times(1)).findUserById(userId);
        verify(taskRepository, times(1)).save(taskEntity);
        verify(taskMapper, times(1)).toTaskEntity(taskPostDto);
        verify(taskMapper, times(1)).toTaskDto(taskEntity);
    }

    @Test
    void testFindAllTasks_Success() {
        List<TaskEntity> taskEntities = List.of(
                new TaskEntity(1L, "Task 1", "Desc 1", TaskStatus.PENDENTE, LocalDate.now(), 1L),
                new TaskEntity(2L, "Task 2", "Desc 2", TaskStatus.CONCLUIDO, LocalDate.now().plusDays(1), 2L)
        );

        List<TaskDto> taskDtos = List.of(
                new TaskDto(1L, "Task 1", "Desc 1", TaskStatus.PENDENTE, LocalDate.now(), 1L),
                new TaskDto(2L, "Task 2", "Desc 2", TaskStatus.CONCLUIDO, LocalDate.now().plusDays(1), 2L)
        );

        when(taskRepository.findAll()).thenReturn(taskEntities);
        when(taskMapper.toTaskDtoList(taskEntities)).thenReturn(taskDtos);

        List<TaskDto> result = taskService.findAllTasks();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(taskDtos, result);

        verify(taskRepository, times(1)).findAll();
        verify(taskMapper, times(1)).toTaskDtoList(taskEntities);
    }

    @Test
    void testDeleteTaskById_Success() {
        Long taskId = 1L;
        Long userId = 1L;

        TaskEntity taskEntity = new TaskEntity(taskId, "Task 1", "Desc 1", TaskStatus.PENDENTE, LocalDate.now(), userId);

        when(userService.findUserById(userId)).thenReturn(new UserEntity());
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
        doNothing().when(taskRepository).deleteById(taskId);

        taskService.deleteTaskById(taskId, userId);

        verify(userService, times(1)).findUserById(userId);
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }
}
