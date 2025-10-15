package dev.luanfernandes.dto.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import dev.luanfernandes.domain.entity.Transaction;
import dev.luanfernandes.dto.request.TransactionRequest;
import dev.luanfernandes.dto.response.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = SPRING, nullValuePropertyMappingStrategy = IGNORE)
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Transaction toEntity(TransactionRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "user.id")
    TransactionResponse toResponse(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(TransactionRequest request, @MappingTarget Transaction transaction);
}
