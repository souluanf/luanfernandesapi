package dev.luanfernandes.dto.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import dev.luanfernandes.domain.entity.User;
import dev.luanfernandes.dto.request.UserRegisterRequest;
import dev.luanfernandes.dto.request.UserUpdateRequest;
import dev.luanfernandes.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = SPRING, nullValuePropertyMappingStrategy = IGNORE)
public interface UserMapper {

    UserResponse toResponse(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    User toEntity(UserRegisterRequest domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void updateEntityFromRequest(UserUpdateRequest request, @MappingTarget User user);
}
