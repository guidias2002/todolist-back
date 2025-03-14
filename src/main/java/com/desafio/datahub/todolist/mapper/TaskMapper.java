package com.desafio.datahub.todolist.mapper;

import com.desafio.datahub.todolist.domain.TaskEntity;
import com.desafio.datahub.todolist.dto.TaskDto;
import com.desafio.datahub.todolist.dto.TaskPostDto;
import org.mapstruct.Mapper;
import org.springframework.scheduling.config.Task;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDto toTaskDto(TaskEntity taskEntity);
    List<TaskDto> toTaskDtoList(List<TaskEntity> taskEntityList);
    TaskEntity toTaskEntity(TaskPostDto taskPostDto);
}
