package com.desafio.datahub.todolist.mapper;

import com.desafio.datahub.todolist.domain.UserEntity;
import com.desafio.datahub.todolist.dto.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toUserEntity(UserDto userDto);
    List<UserDto> toUserDtoList(List<UserEntity> toUserEntityList);
}
