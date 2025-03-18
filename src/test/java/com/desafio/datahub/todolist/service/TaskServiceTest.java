package com.desafio.datahub.todolist.service;

import com.desafio.datahub.todolist.domain.TaskEntity;
import com.desafio.datahub.todolist.domain.UserEntity;
import com.desafio.datahub.todolist.dto.TaskDto;
import com.desafio.datahub.todolist.dto.TaskPostDto;
import com.desafio.datahub.todolist.dto.TaskUpdateDto;
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
import java.util.Arrays;
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
                LocalDate.of(2025, 5, 20),
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

    @Test
    void testFilterTaskByStatus_Success() {
        TaskStatus status = TaskStatus.PENDENTE;

        TaskEntity task1 = new TaskEntity(1L, "Task 1", "Desc 1", TaskStatus.PENDENTE, LocalDate.of(2024, 5, 20), 1L);
        TaskEntity task2 = new TaskEntity(2L, "Task 2", "Desc 2", TaskStatus.PENDENTE, LocalDate.of(2024, 5, 10), 1L);
        TaskEntity task3 = new TaskEntity(3L, "Task 3", "Desc 3", TaskStatus.PENDENTE, LocalDate.of(2024, 5, 15), 1L);

        List<TaskEntity> tasks = Arrays.asList(task1, task2, task3);

        TaskDto taskDto1 = new TaskDto(1L, "Task 1", "Desc 1",  TaskStatus.PENDENTE, LocalDate.of(2024, 5, 20), 1L);
        TaskDto taskDto2 = new TaskDto(2L, "Task 2", "Desc 2", TaskStatus.PENDENTE, LocalDate.of(2024, 5, 10), 1L);
        TaskDto taskDto3 = new TaskDto(3L, "Task 3", "Desc 3", TaskStatus.PENDENTE, LocalDate.of(2024, 5, 15), 1L);

        when(taskRepository.findTasksByStatus(status)).thenReturn(tasks);
        when(taskMapper.toTaskDto(task2)).thenReturn(taskDto2);
        when(taskMapper.toTaskDto(task3)).thenReturn(taskDto3);
        when(taskMapper.toTaskDto(task1)).thenReturn(taskDto1);

        List<TaskDto> result = taskService.filterTaskByStatus(status);

        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals(taskDto2.dueDate(), result.get(0).dueDate());
        assertEquals(taskDto3.dueDate(), result.get(1).dueDate());
        assertEquals(taskDto1.dueDate(), result.get(2).dueDate());

        verify(taskRepository, times(1)).findTasksByStatus(status);
        verify(taskMapper, times(1)).toTaskDto(task1);
        verify(taskMapper, times(1)).toTaskDto(task2);
        verify(taskMapper, times(1)).toTaskDto(task3);
    }

    @Test
    void testOrderByDueDate_Success() {
        TaskEntity task1 = new TaskEntity(1L, "Task 1", "Desc 1", TaskStatus.PENDENTE, LocalDate.of(2024, 5, 20), 1L);
        TaskEntity task2 = new TaskEntity(2L, "Task 2", "Desc 2", TaskStatus.PENDENTE, LocalDate.of(2024, 5, 10), 1L);
        TaskEntity task3 = new TaskEntity(3L, "Task 3", "Desc 3", TaskStatus.PENDENTE, LocalDate.of(2024, 5, 15), 1L);

        List<TaskEntity> tasks = Arrays.asList(task1, task2, task3);

        TaskDto taskDto1 = new TaskDto(1L, "Task 1", "Desc 1",  TaskStatus.PENDENTE, LocalDate.of(2024, 5, 20), 1L);
        TaskDto taskDto2 = new TaskDto(2L, "Task 2", "Desc 2", TaskStatus.PENDENTE, LocalDate.of(2024, 5, 10), 1L);
        TaskDto taskDto3 = new TaskDto(3L, "Task 3", "Desc 3", TaskStatus.PENDENTE, LocalDate.of(2024, 5, 15), 1L);

        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.toTaskDto(task1)).thenReturn(taskDto1);
        when(taskMapper.toTaskDto(task2)).thenReturn(taskDto2);
        when(taskMapper.toTaskDto(task3)).thenReturn(taskDto3);

        List<TaskDto> result = taskService.orderByDueDate();

        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals(taskDto2.dueDate(), result.get(0).dueDate());
        assertEquals(taskDto3.dueDate(), result.get(1).dueDate());
        assertEquals(taskDto1.dueDate(), result.get(2).dueDate());

        verify(taskRepository, times(1)).findAll();
        verify(taskMapper, times(1)).toTaskDto(task1);
        verify(taskMapper, times(1)).toTaskDto(task2);
        verify(taskMapper, times(1)).toTaskDto(task3);
    }

    @Test
    void testUpdateTask_Success() {
        Long taskId = 1L;
        Long userId = 1L;

        TaskEntity existingTask = new TaskEntity(taskId, "Task 1", "Desc 1", TaskStatus.PENDENTE, LocalDate.of(2024, 5, 10), userId);
        TaskUpdateDto taskUpdateDto = new TaskUpdateDto("Task 1 update", "Desc 1 update", LocalDate.now().plusDays(5), TaskStatus.CONCLUIDO);
        TaskDto updatedTaskDto = new TaskDto(taskId, "Task 1 update", "Desc 1 update", TaskStatus.CONCLUIDO, LocalDate.now().plusDays(5), userId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskMapper.toTaskDto(existingTask)).thenReturn(updatedTaskDto);
        when(taskRepository.save(existingTask)).thenReturn(existingTask);
        when(userService.findUserById(userId)).thenReturn(new UserEntity());

        TaskDto result = taskService.updateTask(taskId, userId, taskUpdateDto);

        assertNotNull(result);
        assertEquals("Task 1 update", result.title());
        assertEquals("Desc 1 update", result.description());
        assertEquals(TaskStatus.CONCLUIDO, result.status());
        assertEquals(taskUpdateDto.dueDate(), result.dueDate());

        verify(userService, times(1)).findUserById(userId);
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(existingTask);
        verify(taskMapper, times(1)).toTaskDto(existingTask);
    }
}
