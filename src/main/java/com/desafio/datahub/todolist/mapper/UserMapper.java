package com.desafio.datahub.todolist.mapper;

import com.desafio.datahub.todolist.domain.UserEntity;
import com.desafio.datahub.todolist.dto.UserDto;
import com.desafio.datahub.todolist.dto.UserPostDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toUserEntity(UserPostDto userPostDto);
    List<UserDto> toUserDtoList(List<UserEntity> toUserEntityList);
    UserDto toUserDto(UserEntity userEntity);
}
