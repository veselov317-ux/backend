package com.backend.sys.service;

import com.backend.sys.dto.request.LoginRequest;
import com.backend.sys.dto.request.RegisterRequest;
import com.backend.sys.dto.request.UserCreateRequest;
import com.backend.sys.dto.response.AuthResponse;
import com.backend.sys.dto.response.UserResponse;
import com.backend.sys.entity.Role;
import com.backend.sys.entity.User;
import com.backend.sys.exception.ResourceConflictException;
import com.backend.sys.repository.UserRepository;
import com.backend.sys.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final ResponseMapper mapper;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider,
            ResponseMapper mapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.mapper = mapper;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        User user = createUser(request.fullName(), request.email(), request.password(), Role.USER);
        return mapper.toUser(user);
    }

    @Transactional
    public UserResponse createAccount(UserCreateRequest request) {
        if (request.role() == Role.USER) {
            return mapper.toUser(createUser(request.fullName(), request.email(), request.password(), Role.USER));
        }
        return mapper.toUser(createUser(request.fullName(), request.email(), request.password(), request.role()));
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        String token = tokenProvider.generateToken(authentication);
        User user = userRepository.findByEmailIgnoreCase(request.email()).orElseThrow();
        return new AuthResponse(token, "Bearer", mapper.toUser(user));
    }

    private User createUser(String fullName, String email, String password, Role role) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ResourceConflictException("Email is already registered");
        }
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }
}
