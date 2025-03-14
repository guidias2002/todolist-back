package com.desafio.datahub.todolist.repository;

import com.desafio.datahub.todolist.domain.TaskEntity;
import com.desafio.datahub.todolist.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findTasksByStatus(TaskStatus status);
}
