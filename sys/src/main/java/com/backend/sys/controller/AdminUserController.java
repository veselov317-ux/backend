package com.backend.sys.controller;

import com.backend.sys.dto.request.UserCreateRequest;
import com.backend.sys.dto.response.UserResponse;
import com.backend.sys.repository.UserRepository;
import com.backend.sys.service.AuthService;
import com.backend.sys.service.ResponseMapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final ResponseMapper mapper;

    public AdminUserController(AuthService authService, UserRepository userRepository, ResponseMapper mapper) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(mapper::toUser)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody UserCreateRequest request) {
        return authService.createAccount(request);
    }
}
