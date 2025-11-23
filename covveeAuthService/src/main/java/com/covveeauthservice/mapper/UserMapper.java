package com.covveeauthservice.mapper;

import com.covveeauthservice.dto.CreateUserDto;
import com.covveeauthservice.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(CreateUserDto createUserDto);

    CreateUserDto toDto(User user);
}
