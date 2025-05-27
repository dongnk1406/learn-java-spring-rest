package com.stephen.spring_boot_api.mapper;

import org.mapstruct.Mapper;

import com.stephen.spring_boot_api.dto.response.UserResponse;
import com.stephen.spring_boot_api.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
