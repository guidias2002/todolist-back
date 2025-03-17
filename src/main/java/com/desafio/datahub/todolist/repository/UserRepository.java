package com.desafio.datahub.todolist.repository;

import com.desafio.datahub.todolist.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);
    boolean existsById(Long id);
    Optional<UserEntity> findByEmail(String email);
}
