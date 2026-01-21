package com.stephen.spring_boot_api.service;

import java.util.List;

import com.stephen.spring_boot_api.dto.request.UserCreationRequest;
import com.stephen.spring_boot_api.dto.request.UserUpdateRequest;
import com.stephen.spring_boot_api.dto.response.UserResponse;
import com.stephen.spring_boot_api.entity.User;

public interface UserService {
    User createUser(UserCreationRequest request);

    List<User> getUsers();

    User getUser(String userId);

    User updateUser(String userId, UserUpdateRequest request);

    void deleteUser(String userId);

    UserResponse getMyInfo();
}
